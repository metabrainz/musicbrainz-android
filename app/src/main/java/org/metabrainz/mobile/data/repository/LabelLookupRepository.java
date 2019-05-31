package org.metabrainz.mobile.data.repository;

import androidx.lifecycle.MutableLiveData;

import org.metabrainz.mobile.api.webservice.Constants;
import org.metabrainz.mobile.data.sources.api.LookupService;
import org.metabrainz.mobile.data.sources.api.MusicBrainzServiceGenerator;
import org.metabrainz.mobile.data.sources.api.entities.CoverArt;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Label;
import org.metabrainz.mobile.data.sources.api.entities.mbentity.Release;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LabelLookupRepository {
    private final static LookupService service = MusicBrainzServiceGenerator
            .createService(LookupService.class, true);
    private static LabelLookupRepository repository;
    private static MutableLiveData<Label> labelData;

    private LabelLookupRepository() {

        labelData = new MutableLiveData<>();
    }

    public static LabelLookupRepository getRepository() {
        if (repository == null) repository = new LabelLookupRepository();
        return repository;
    }

    public static void destroyRepository() {
        repository = null;
    }

    public MutableLiveData<Label> initializeLabelData() {
        return labelData;
    }

    public void getLabel(String MBID) {
        service.lookupLabel(MBID, Constants.LOOKUP_LABEL_PARAMS).enqueue(new Callback<Label>() {
            @Override
            public void onResponse(Call<Label> call, Response<Label> response) {
                Label Label = response.body();
                labelData.setValue(Label);
            }

            @Override
            public void onFailure(Call<Label> call, Throwable t) {

            }
        });
    }

    /**
     * For a given release ID, fetches the cover arts and updates the release w¡th that info
     *
     * @param release Release for which the cover art is to be retrieved
     */
    public Single<CoverArt> fetchCoverArtForRelease(Release release) {
        return service.getCoverArt(release.getMbid());
    }
}
