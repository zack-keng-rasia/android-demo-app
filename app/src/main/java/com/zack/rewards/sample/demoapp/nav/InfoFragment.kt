package com.zack.rewards.sample.demoapp.nav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.zack.rewards.sample.demoapp.R

/**
 *
 * @author zack.keng
 * Created on 2023/08/18
 * Copyright © 2023 Rakuten Asia. All rights reserved.
 */
abstract class InfoFragment : Fragment() {
    abstract fun createFragmentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View

    abstract fun infoIconClicked()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return createFragmentView(inflater, container, savedInstanceState)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.info_menu, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_info) {
            infoIconClicked()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    protected fun showInfoDialog(message: String, title: String? = null) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton("Close", null)
            .show()
    }
}