package com.DI.Component

import com.DI.Modules.ContentFragmentModule
import com.DI.Scopes.ContentFragmentScope
import com.View.Fragments.ContentFragment
import dagger.Subcomponent

@ContentFragmentScope
@Subcomponent(modules = [ContentFragmentModule::class])
interface CuentaSubComponent {
    fun inject(contentFragment: ContentFragment)
}