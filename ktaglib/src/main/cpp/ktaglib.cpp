
#include <jni.h>
#include <sys/stat.h>
#include <fileref.h>
#include <toolkit/tiostream.h>
#include <toolkit/tfilestream.h>
#include <toolkit/tpicture.h>
#include <toolkit/tpicturemap.h>

jclass globalSongClass;
jmethodID songInit;

jclass globalIntClass;
jmethodID intInit;

jmethodID intGetValue;

extern "C" JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }

    jclass songClass = env->FindClass("com/musicbrainz/ktaglib/AudioFile");
    globalSongClass = reinterpret_cast<jclass>(env->NewGlobalRef(songClass));
    env->DeleteLocalRef(songClass);
    songInit = env->GetMethodID(
            globalSongClass,
            "<init>",
            "(Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/Integer;Ljava/lang/String;)V"
    );

    jclass intClass = env->FindClass("java/lang/Integer");
    globalIntClass = reinterpret_cast<jclass>(env->NewGlobalRef(intClass));
    env->DeleteLocalRef(intClass);
    intInit = env->GetMethodID(globalIntClass, "<init>", "(I)V");

    intGetValue = env->GetMethodID(globalIntClass, "intValue", "()I");

    return JNI_VERSION_1_6;
}

extern "C" void JNI_OnUnload(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6);

    env->DeleteGlobalRef(globalSongClass);
    env->DeleteGlobalRef(globalIntClass);
}


extern "C"
JNIEXPORT jobject JNICALL
Java_com_musicbrainz_ktaglib_KTagLib_getAudioFile(JNIEnv *env, jobject thiz, jint fd_, jstring pathStr,
                                          jstring fileName) {

    int fd = (int) fd_;

    jstring title = fileName;
    jstring artist = nullptr;
    jstring albumArtist = nullptr;
    jstring album = nullptr;
    jstring genre = nullptr;

    jobject track = nullptr;
    jobject trackTotal = nullptr;
    jobject disc = nullptr;
    jobject discTotal = nullptr;
    jobject duration = nullptr;
    jobject year = nullptr;

    TagLib::IOStream *stream = new TagLib::FileStream(fd, true);
    TagLib::FileRef fileRef(stream);

    if (!fileRef.isNull()) {

        if (fileRef.audioProperties()) {
            TagLib::AudioProperties *properties = fileRef.audioProperties();
            duration = env->NewObject(globalIntClass, intInit, properties->lengthInMilliseconds());
        }

        if (fileRef.tag()) {
            TagLib::Tag *tag = fileRef.tag();

            if (!tag->title().isEmpty()) {
                title = env->NewStringUTF(tag->title().toCString(true));
            }

            if (!tag->artist().isEmpty()) {
                artist = env->NewStringUTF(tag->artist().toCString(true));
            }

            const TagLib::PropertyMap &properties = tag->properties();

            albumArtist = artist;
            if (properties.contains("ALBUMARTIST")) {
                const TagLib::StringList &stringList = properties["ALBUMARTIST"];
                if (!stringList.isEmpty()) {
                    albumArtist = env->NewStringUTF(stringList.front().toCString(true));
                }
            }

            if (!tag->album().isEmpty()) {
                album = env->NewStringUTF(tag->album().toCString(true));
            }

            track = env->NewObject(globalIntClass, intInit, (int) tag->track());

            if (properties.contains("DISCNUMBER")) {
                const TagLib::StringList &stringList = properties["DISCNUMBER"];
                if (!stringList.isEmpty()) {
                    disc = env->NewObject(globalIntClass, intInit, stringList.front().toInt());
                }
            }

            year = env->NewObject(globalIntClass, intInit, (int) tag->year());

            if (!tag->genre().isEmpty()) {
                genre = env->NewStringUTF(tag->genre().toCString(true));
            }
        }

        struct stat statbuf{};
        fstat(fd, &statbuf);

        jobject song = env->NewObject(
                globalSongClass,
                songInit,
                pathStr,
                statbuf.st_size,
                (long) statbuf.st_mtime * 1000L,
                title,
                albumArtist,
                artist,
                album,
                track,
                trackTotal,
                disc,
                discTotal,
                duration,
                year,
                genre
        );
        return song;
    }
    return nullptr;
}
//extern "C"
//JNIEXPORT jbyteArray JNICALL
//Java_com_musicbrainz_ktaglib_KTagLib_getArtwork(JNIEnv *env, jobject thiz, jint fd_) {
//
//    int fd = (int) fd_;
//
//    TagLib::IOStream *stream = new TagLib::FileStream(fd, true);
//    TagLib::FileRef fileRef(stream);
//
//    if (!fileRef.isNull()) {
//        TagLib::Tag *tag = fileRef.tag();
//
//        TagLib::PictureMap pictureMap = tag->pictures();
//
//        TagLib::Picture picture;
//
//        // Finds the largest picture by byte size
//        size_t picSize = 0;
//        for (auto const &x: pictureMap) {
//            for (auto const &y: x.second) {
//                size_t size = y.data().size();
//                if (size > picSize) {
//                    picture = y;
//                }
//            }
//        }
//
//        TagLib::ByteVector byteVector = picture.data();
//        size_t len = byteVector.size();
//        if (len > 0) {
//            jbyteArray arr = env->NewByteArray(len);
//            char *data = byteVector.data();
//            env->SetByteArrayRegion(arr, 0, len, reinterpret_cast<jbyte *>(data));
//            return arr;
//        }
//    }
//
//    return nullptr;
//
//}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_musicbrainz_ktaglib_KTagLib_updateTags(JNIEnv *env, jobject thiz,
                                        jint fd_,
                                        jstring title_,
                                        jstring artist_,
                                        jstring album_,
                                        jstring albumArtist_,
                                        jobject year_,
                                        jobject track_,
                                        jobject trackTotal_,
                                        jobject disc_,
                                        jobject discTotal_,
                                        jstring genre_) {

    int fd = (int) fd_;

    TagLib::IOStream *stream = new TagLib::FileStream(fd, false);
    TagLib::FileRef fileRef(stream);

    if (!fileRef.isNull()) {
        TagLib::Tag *tag = fileRef.tag();
        if (tag) {

            if (title_) {
                const char *title = env->GetStringUTFChars(title_, 0);
                tag->setTitle(title);
                env->ReleaseStringUTFChars(title_, title);
            }

            if (artist_) {
                const char *artist = env->GetStringUTFChars(artist_, 0);
                tag->setArtist(artist);
                env->ReleaseStringUTFChars(artist_, artist);
            }

            if (album_) {
                const char *album = env->GetStringUTFChars(album_, 0);
                tag->setAlbum(album);
                env->ReleaseStringUTFChars(album_, album);
            }

            TagLib::PropertyMap properties = TagLib::PropertyMap();

            if (albumArtist_) {
                const char *albumArtist = env->GetStringUTFChars(albumArtist_, 0);
                TagLib::StringList stringList = TagLib::StringList();
                stringList.append(albumArtist);
                properties.insert("ALBUMARTIST", stringList);
                env->ReleaseStringUTFChars(albumArtist_, albumArtist);
            }

            if (year_) {
                tag->setYear(env->CallIntMethod(year_, intGetValue));
            }

            if (track_) {
                int track = env->CallIntMethod(track_, intGetValue);
                std::string trackString = std::to_string(track);

                if (trackTotal_) {
                    int trackTotal = env->CallIntMethod(trackTotal_, intGetValue);
                    trackString += "/";
                    trackString += std::to_string(trackTotal);
                }

                TagLib::StringList stringList = TagLib::StringList();
                stringList.append(trackString);

                properties.insert("TRACKNUMBER", stringList);
            }

            if (disc_) {
                int disc = env->CallIntMethod(disc_, intGetValue);
                std::string discString = std::to_string(disc);

                if (discTotal_) {
                    int discTotal = env->CallIntMethod(discTotal_, intGetValue);
                    discString += "/";
                    discString += std::to_string(discTotal);
                }

                TagLib::StringList stringList = TagLib::StringList();
                stringList.append(discString);

                properties.insert("DISCNUMBER", stringList);
            }

            if (genre_) {
                const char *genre = env->GetStringUTFChars(genre_, 0);
                tag->setGenre(genre);
                env->ReleaseStringUTFChars(genre_, genre);
            }

            if (!properties.isEmpty()) {
                tag->setProperties(properties);
            }
        }
        return fileRef.save();
    }
    return false;


}