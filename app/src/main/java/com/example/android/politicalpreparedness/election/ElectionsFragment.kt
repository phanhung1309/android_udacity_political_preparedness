package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.models.Election

class ElectionsFragment : Fragment() {

    private val viewModel by viewModels<ElectionsViewModel> {
        ElectionsViewModelFactory(
            ElectionDatabase.getInstance(requireContext()).electionDao
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )
            : View? {
        val binding = FragmentElectionBinding.inflate(inflater)

        with(binding) {
            viewModel = this@ElectionsFragment.viewModel
            lifecycleOwner = this@ElectionsFragment
            upcomingElectionsRecyclerView.adapter =
                ElectionListAdapter(ElectionListener {
                    this@ElectionsFragment.viewModel.displayVoterInfo(it)
                })
            savedElectionsRecyclerView.adapter =
                ElectionListAdapter(ElectionListener {
                    this@ElectionsFragment.viewModel.displayVoterInfo(it)
                })
        }

        viewModel.navigateToVoterInfo.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                navigateToDetailFragment(it)
                viewModel.displayVoterInfoComplete()
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshElections()
    }

    private fun navigateToDetailFragment(election: Election) {
        this.findNavController().navigate(
            ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                election.id,
                election.division
            )
        )
    }

    // TODO: Refresh adapters when fragment loads
}