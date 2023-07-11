package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private val viewModel: VoterInfoViewModel by viewModels {
        VoterInfoViewModelFactory(
            ElectionDatabase.getInstance(requireContext()).electionDao
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val voterInfoFragmentArgs = VoterInfoFragmentArgs.fromBundle(requireArguments())
        val electionId = voterInfoFragmentArgs.argElectionId
        val division = voterInfoFragmentArgs.argDivision

        viewModel.getVoterInformation(electionId, division)

        viewModel.url.observe(viewLifecycleOwner, Observer { url ->
            url?.let {
                browseUrl(it)
                viewModel.navigateToUrlComplete()
            }
        })

        return binding.root
    }

    private fun browseUrl(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
}