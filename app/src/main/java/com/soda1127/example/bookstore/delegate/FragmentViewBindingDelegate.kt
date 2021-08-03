package com.soda1127.example.bookstore.delegate

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author SODA1127
 * ViewBinding 사용 시 Delegate 패턴을 이용하여 Fragment에 inflate할 뷰바인딩 인스턴스를 생성함.
 * private val binding: FragmentMainBinding by viewBinding()
 * reference : https://zhuinden.medium.com/simple-one-liner-viewbinding-in-fragments-and-activities-with-kotlin-961430c6c07c
 */
inline fun <reified T : ViewBinding> Fragment.viewBinding() = FragmentViewBindingDelegate(T::class.java, this)

class FragmentViewBindingDelegate<T : ViewBinding>(
    private val bindingClass: Class<T>,
    private val fragment: Fragment
) : ReadOnlyProperty<Fragment, T> {

    /**
     * initiate variable for binding view
     */
    private var binding: T? = null

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        binding?.let { return it }

        /**
         * inflate View class
         */
        val inflateMethod = bindingClass.getMethod("inflate", LayoutInflater::class.java)

        /**
         * Adding observer to the fragment lifecycle
         */
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
                    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            /**
                             * Clear the binding when Fragment lifecycle called the onDestroy
                             *
                             * Fragment에서 Navigation component 또는 BackStack or detach를 사용하는 경우,
                             * onDestroyView() 이후에 Fragment view는 종료되지만, Fragment는 여전히 살아 있습니다.
                             * 즉 메모리 누수가 발생하게 됩니다. 그래서 binding변수를 onDestroyView 이후에 null로 해제해주어야 합니다.
                             *
                             * https://developer.android.com/topic/libraries/view-binding#fragments
                             */
                            binding = null
                        }
                    })
                }
            }
        })


        /**
         * Checking the fragment lifecycle
         */
        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            error("Cannot access view bindings. Current lifecycle state is ${lifecycle.currentState}!")
        }


        /**
         * Bind layout
         */
        val invoke = inflateMethod.invoke(null, LayoutInflater.from(thisRef.context)) as T

        return invoke.also { this.binding = it }
    }
}

