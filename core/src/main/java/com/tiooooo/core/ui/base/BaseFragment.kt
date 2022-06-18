package com.tiooooo.core.ui.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.tiooooo.core.ui.custom.LoadingDialog.displayLoadingDialog
import com.tiooooo.core.ui.custom.LoadingDialog.hideLoadingDialog
import java.lang.reflect.ParameterizedType

abstract class BaseFragment<out VB : ViewBinding, out A : Activity>(private val layoutId: Int) : Fragment() {
    val parentActivity: A by lazy { activity as A }
    private var _binding: VB? = null
    val binding get() = _binding!!

    @LayoutRes
    fun getLayout(): Int = layoutId

    protected open fun initView() {}
    protected open fun initListener() {}
    protected open fun setSubscribeToLiveData() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val type = javaClass.genericSuperclass
        val clazz = (type as? ParameterizedType)?.actualTypeArguments?.get(0) as Class<*>
        val method = clazz.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )

        _binding = method.invoke(null, inflater, container, false) as VB
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        setSubscribeToLiveData()
    }

    fun populateLoadingDialog(state: Boolean? = false) {
        if (state == true) parentActivity.displayLoadingDialog()
        else parentActivity.hideLoadingDialog()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}