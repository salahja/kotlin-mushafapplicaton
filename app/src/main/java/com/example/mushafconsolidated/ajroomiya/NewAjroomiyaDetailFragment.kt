package com.example.mushafconsolidated.ajroomiya

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mushafconsolidated.Entities.GrammarRules
import com.example.mushafconsolidated.R
import com.example.mushafconsolidated.ajroomiya.placeholder.AjroomiyaRulecontents
import com.example.mushafconsolidated.databinding.NewFragmentAjroomiyaDetailBinding

import com.google.android.material.appbar.CollapsingToolbarLayout
import org.sj.conjugator.activity.BaseActivity

/**
 * A fragment representing a single GrammarRule detail screen.

 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
class NewAjroomiyaDetailFragment
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
    : Fragment() {
    /**
     * The placeholder content this fragment is presenting.
     */
    private var mItem: GrammarRules? = null
    private var mToolbarLayout: CollapsingToolbarLayout? = null
    private lateinit var mTextView: WebView
    private var _binding: NewFragmentAjroomiyaDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (requireArguments().containsKey(ARG_ITEM_ID)) {
            // Load the placeholder content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = AjroomiyaRulecontents.ITEM_MAP[requireArguments().getString(ARG_ITEM_ID)]
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = NewFragmentAjroomiyaDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the current theme (adjust this based on how you share data between activity and fragment)
        val currentTheme = arguments?.getString("currentTheme") ?: BaseActivity.BROWN_MODE

        // Apply background color based on theme
        val backgroundColor = when (currentTheme){
            BaseActivity.BROWN_MODE -> ContextCompat.getColor(requireContext(), R.color.bg_brown)
            else -> ContextCompat.getColor(requireContext(), R.color.md_db__theme_dark_background)
        }
        view.setBackgroundColor(backgroundColor)

        mTextView=binding.ajroomiyaDetail
        mToolbarLayout=binding.toolbarLayout
        mTextView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        mTextView.settings.javaScriptEnabled = true
        mTextView.settings.domStorageEnabled = true
        mTextView.settings.loadWithOverviewMode = true
        mTextView.settings.useWideViewPort = true
        mTextView.settings.builtInZoomControls = true


       mTextView.setInitialScale(3)
        mTextView.settings.loadWithOverviewMode = true
        mTextView.settings.useWideViewPort = true

        mTextView.settings.builtInZoomControls = true
        // Show the placeholder content as text in a TextView & in the toolbar if available.
        updateContent()

       // root.setOnDragListener(dragListener)
        // Implement additional logic here as needed
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateContent() {
        if (mItem != null) {
            mTextView.loadDataWithBaseURL(
                null, mItem!!.detailsrules, "text/html", "utf-8", null)
            //   mTextView.setText(mItem.getDetailsrules());
            if (mToolbarLayout != null) {
                mToolbarLayout!!.title = mItem!!.worddetails
            }
        }
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }


}