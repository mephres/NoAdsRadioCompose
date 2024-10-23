package com.intas.monitortsom.presentation.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.intas.monitortsom.presentation.auth.AuthComponent
import com.intas.monitortsom.presentation.main.MainComponent
import com.intas.monitortsom.presentation.options.OptionsComponent
import com.intas.monitortsom.presentation.pdf_view.PdfViewComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Auth(val component: AuthComponent) : Child
        data class Options(val component: OptionsComponent) : Child
        data class Main(val component: MainComponent) : Child
        data class PdfView(val component: PdfViewComponent) : Child
    }
}