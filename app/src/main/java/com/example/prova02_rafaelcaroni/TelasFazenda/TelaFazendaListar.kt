package com.example.prova02_rafaelcaroni.TelasFazenda

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.prova02_rafaelcaroni.Classes.Fazenda
import com.example.prova02_rafaelcaroni.Controller.FazendaController
import com.example.prova02_rafaelcaroni.TelasFazenda.ui.theme.Prova02_RafaelCaroniTheme
import kotlinx.coroutines.launch

class TelaFazendaListar : ComponentActivity() {
    lateinit var listaFazendas: ArrayList<Fazenda>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fazendaController = FazendaController()
        lifecycleScope.launch {
            try {
                listaFazendas = fazendaController.carregarListaFazendas()
                setContent {
                    Prova02_RafaelCaroniTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            listarFazendas(listaFazendas)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.i("errorCarregarListaFazendas", "${e}")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun listarFazendas(listaFazendas: ArrayList<Fazenda>) {
    val contexto: Context = LocalContext.current
    val fazendaController = FazendaController()
    val activity: Activity? = (LocalContext.current as? Activity)
    val requestActivityResult = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // O resultado está OK, recarregue a página aqui
            activity?.recreate()
        } else {
            // O resultado não está OK, tratamento de erro ou outra lógica aqui
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp, 5.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(Modifier.padding(80.dp, 0.dp)) {
                    Text(
                        text = "Gestão de Fazendas",
                        fontSize = 24.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(innerPadding)
                    )
                    Text(
                        text = "By - Rafael Caroni", fontSize = 10.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(listaFazendas) { fazenda ->
                            Card(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(0.dp, 8.dp),
                                elevation = CardDefaults.cardElevation(8.dp)
                            ) {
                                Card(
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(10.dp),
                                    elevation = CardDefaults.cardElevation(12.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp, 8.dp)) {
                                        Text(
                                            text = "${fazenda.nome}",
                                            style = typography.titleMedium,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .align(Alignment.CenterHorizontally)
                                                .padding(2.dp)
                                        )
                                        Text(
                                            text = "Codigo: ${fazenda.codigo}",
                                            style = typography.bodyMedium
                                        )
                                        Text(
                                            text = "Valor da Propriedade: R$${fazenda.valorDaPropriedade}",
                                            style = typography.bodyMedium
                                        )
                                        Text(
                                            text = "Qntd de Funcionários: ${fazenda.qntdFuncionarios}",
                                            style = typography.bodyMedium
                                        )
                                    }
                                }
                                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                    IconButton(onClick = {
                                        showConfirmationDialog(contexto) {
                                            fazendaController.excluirFazenda(
                                                fazenda.codigo.toString(),
                                                contexto
                                            ) { sucesso ->
                                                if (sucesso) {
                                                    Toast.makeText(
                                                        contexto,
                                                        "Cliente deletado com sucesso!",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    activity?.recreate()
                                                } else {

                                                }
                                            }
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Delete,
                                            contentDescription = "Deletar Fazenda"
                                        )
                                    }
                                    IconButton(onClick = {
                                        requestActivityResult.launch(
                                            Intent(contexto, TelaFazendaInserir::class.java).apply {
                                                putExtra("fazenda", fazenda)
                                            }
                                        )
                                    }) {
                                        Icon(
                                            imageVector = Icons.Outlined.Edit,
                                            contentDescription = "Editar Fazenda"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

private fun showConfirmationDialog(context: Context, onConfirmation: () -> Unit) {
    AlertDialog.Builder(context)
        .setTitle("Confirmação")
        .setMessage("Tem certeza de que deseja excluir esta Fazenda ?")
        .setPositiveButton("Sim") { _, _ ->
            onConfirmation.invoke()
        }
        .setNegativeButton("Não", null)
        .show()
}


