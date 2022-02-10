package com.cartrack.userlocation.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cartrack.userlocation.R
import com.cartrack.userlocation.SessionManagerUtil
import com.cartrack.userlocation.data.api.model.UserInfoRepositoryList
import com.cartrack.userlocation.databinding.ClickListener
import com.cartrack.userlocation.databinding.UserDetailsLayoutBinding
import com.cartrack.userlocation.viewmodel.UserDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailFragment : Fragment(), ClickListener {
    companion object {
        private const val TAG = "Ro_UserDetailFragment"
        fun newInstance(): UserDetailFragment {
            return UserDetailFragment()
        }
    }

    private lateinit var binding: UserDetailsLayoutBinding
    private lateinit var activity: Activity
    private val viewModel: UserDetailViewModel by viewModels()
    private lateinit var recyclerViewUserDataAdapter: RecyclerViewUserDataAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")
        if (context is Activity) activity = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (requireActivity() as AppCompatActivity).supportActionBar?.show()

        binding = DataBindingUtil.inflate(inflater, R.layout.user_details_layout, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        val appCompatActivity = activity
        if (appCompatActivity is AppCompatActivity) {
            appCompatActivity.setSupportActionBar(binding.toolbarUserdetails)
            appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        }

        binding.recyclerview.apply {

            recyclerViewUserDataAdapter = RecyclerViewUserDataAdapter(this@UserDetailFragment)


            binding.recyclerview.layoutManager = LinearLayoutManager(
                activity, RecyclerView.VERTICAL, false
            )
            binding.recyclerview.adapter = recyclerViewUserDataAdapter

        }

        viewModel.allWords.observe(viewLifecycleOwner, Observer { list
            ->
            list.let {
                recyclerViewUserDataAdapter.submitList(it as MutableList<UserInfoRepositoryList>)
                recyclerViewUserDataAdapter.notifyDataSetChanged()

            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                Toast.makeText(activity, "logout", Toast.LENGTH_LONG).show()
                SessionManagerUtil.endUserSession(activity)
                startActivity(LoginActivity.createIntent(activity))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View?) {
        //not needed here
    }

    override fun onRecyclerViewClick(data: UserInfoRepositoryList?) {
        Log.d(TAG,"clicked item loc ------ "+data?.address?.geo?.lat)
        startActivity(UserLocationActivity.createIntent(activity, data))
    }

    override fun getLocation() {

    }
}