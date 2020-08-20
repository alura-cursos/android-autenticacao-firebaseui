package br.com.alura.aluraesporte

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.alura.aluraesporte.extensions.snackBar
import br.com.alura.aluraesporte.ui.fragment.LoginFragmentDirections
import br.com.alura.aluraesporte.ui.viewmodel.ComponentesVisuais
import br.com.alura.aluraesporte.ui.viewmodel.EstadoAppViewModel
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import kotlinx.android.synthetic.main.inicio.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

private const val RC_SIGN_IN = 1
private const val TAG = "InicioFragment"

class InicioFragment : Fragment() {

    private val controlador by lazy {
        findNavController()
    }
    private val estadoAppViewModel: EstadoAppViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.inicio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        estadoAppViewModel.temComponentes = ComponentesVisuais()
        inicio_botao_entrar.setOnClickListener {
            val authUi = AuthUI.getInstance()
            val intent = authUi.createSignInIntentBuilder()
                .setAvailableProviders(listOf(AuthUI.IdpConfig.EmailBuilder().build()))
                .build()
            startActivityForResult(intent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                vaiParaListaProdutos()
            } else {
                val response = IdpResponse.fromResultIntent(data)
                Log.e(TAG, "onActivityResult: falha ao autenticar", response?.error)
                view?.snackBar("Falha ao autenticar")
            }
        }
    }

    private fun vaiParaListaProdutos() {
        val direcao = InicioFragmentDirections.acaoInicioParaListaProdutos()
        controlador.navigate(direcao)
    }

}