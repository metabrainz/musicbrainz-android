package org.metabrainz.mobile.presentation.features.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.thefinestartist.finestwebview.FinestWebView
import dagger.hilt.android.AndroidEntryPoint
import org.metabrainz.mobile.R
import org.metabrainz.mobile.databinding.ActivityDashboardBinding
import org.metabrainz.mobile.presentation.IntentFactory
import org.metabrainz.mobile.presentation.UserPreferences.advancedFeaturesPreference
import org.metabrainz.mobile.presentation.features.about.AboutActivity
import org.metabrainz.mobile.presentation.features.barcode.BarcodeActivity
import org.metabrainz.mobile.presentation.features.collection.CollectionActivity
import org.metabrainz.mobile.presentation.features.search.SearchActivity
import org.metabrainz.mobile.presentation.features.tagger.TaggerActivity

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    var viewModel: PaymentViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        viewModel = ViewModelProvider(this).get(PaymentViewModel::class.java)

        //showing the title only when collapsed
        var isShow = true
        var scrollRange = -1
        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }
            if (scrollRange + verticalOffset == 0) {
                binding.colToolbarId.title = "MusicBrainz"
                isShow = true
            } else if (isShow) {
                binding.colToolbarId.title = " "
                isShow = false
            }
        })
        setSupportActionBar(binding.toolbar)

        //navigation
        binding.dashboardTagId.setOnClickListener {
            startActivity(Intent(this, TaggerActivity::class.java))
        }
        binding.dashboardAboutId.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
        binding.dashboardSearchId.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
        binding.dashboardCollectionId.setOnClickListener {
            startActivity(Intent(this, CollectionActivity::class.java))
        }
        binding.dashboardDonateId.setOnClickListener {
            prepareCheckout { customerConfig, clientSecret ->
                paymentSheet.presentWithPaymentIntent(
                    clientSecret,
                    PaymentSheet.Configuration(
                        merchantDisplayName = merchantName,
                        customer = customerConfig,
                        googlePay = googlePayConfig,
                        allowsDelayedPaymentMethods = true
                    )
                )
            }
        }
        binding.dashboardScanId.setOnClickListener {
            startActivity(Intent(this, BarcodeActivity::class.java))
        }
        binding.dashboardListenId.setOnClickListener {
            FinestWebView.Builder(this).show("https://listenbrainz.org/");
        }
        binding.dashboardCritiqueId.setOnClickListener {
            FinestWebView.Builder(this).show("https://critiquebrainz.org/");
        }


        //cardview animation
        val leftItemAnimation = AnimationUtils.loadAnimation(this, R.anim.left_dashboard_item_animation)
        val rightItemAnimation = AnimationUtils.loadAnimation(this, R.anim.right_dashboard_item_animation)
        binding.dashboardTagId.animation = leftItemAnimation
        binding.dashboardSearchId.animation = rightItemAnimation
        binding.dashboardScanId.animation = rightItemAnimation
        binding.dashboardDonateId.animation = leftItemAnimation
        binding.dashboardAboutId.animation = rightItemAnimation
        binding.dashboardCollectionId.animation = leftItemAnimation
        binding.dashboardCritiqueId.animation = leftItemAnimation
        binding.dashboardListenId.animation = rightItemAnimation

    }

    override fun onResume() {
        super.onResume()
        if(advancedFeaturesPreference){
            binding.advancedFeatures.visibility = VISIBLE
        }
        else{
            binding.advancedFeatures.visibility = GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dash, menu)
        menu?.findItem(R.id.menu_open_website)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_login -> {
                startActivity(IntentFactory.getLogin(this))
                true
            }
            R.id.menu_preferences -> {
                startActivity(IntentFactory.getSettings(this))
                true
            }
            R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun prepareCheckout(onSuccess: (PaymentSheet.CustomerConfiguration?, String) -> Unit) {
        viewModel!!.fetchPaymentIntent("https://stripe-server-akshaaatt.herokuapp.com/checkout").observe(this) { checkoutResponse ->
            // Init PaymentConfiguration with the publishable key returned from the backend,
            // which will be used on all Stripe API calls
            PaymentConfiguration.init(this, checkoutResponse.data?.publishableKey!!)

            onSuccess(
                checkoutResponse.data.makeCustomerConfig(),
                checkoutResponse.data.paymentIntent
            )

            viewModel!!.fetchPaymentIntent("https://stripe-server-akshaaatt.herokuapp.com/checkout").removeObservers(this)
        }
    }

    private fun onPaymentSheetResult(paymentResult: PaymentSheetResult) {
//        viewModel!!. = paymentResult.toString()
    }

    companion object {
        const val merchantName = "METABRAINZ FOUNDATION"
        const val backendUrl = "https://stripe-server-akshaaatt.herokuapp.com/checkout"
        val googlePayConfig = PaymentSheet.GooglePayConfiguration(
            environment = PaymentSheet.GooglePayConfiguration.Environment.Production,
            countryCode = "US"
        )
    }
}