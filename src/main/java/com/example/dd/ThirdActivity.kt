package com.example.dd

import Personagem.CriarPersonagem
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dd.ui.theme.DDTheme
import com.example.dd.data.DatabaseHelper

class ThirdActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recupera os dados do personagem vindo da intent
        val personagem = intent.getSerializableExtra("personagem") as? CriarPersonagem

        setContent {
            DDTheme {
                PersonagemScreen(personagem = personagem)
            }
        }
    }
}

@Composable
fun PersonagemScreen(personagem: CriarPersonagem?) {
    val context = LocalContext.current

    if (personagem == null) {
        Text("Personagem não encontrado.")
        return
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        // Exibindo detalhes do personagem
        Text(text = "Nome: ${personagem.nome}", style = MaterialTheme.typography.titleLarge)
        Text(text = "Raça: ${personagem.raca}", style = MaterialTheme.typography.titleMedium)
        Text(text = "Força: ${personagem.forca}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Inteligência: ${personagem.inteligencia}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Destreza: ${personagem.destreza}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Sabedoria: ${personagem.sabedoria}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Constituição: ${personagem.constituicao}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Carisma: ${personagem.carisma}", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para salvar o personagem no banco de dados
        Button(
            onClick = {
                // Inicializa o DatabaseHelper e salva o personagem
                val dbHelper = DatabaseHelper(context)
                val saved = dbHelper.insertCharacter(personagem)

                if (saved != -1L) {
                    Toast.makeText(context, "Personagem salvo com sucesso", Toast.LENGTH_SHORT).show()
                    // Redireciona para a MainActivity
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "Erro ao salvar personagem", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar Personagem")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPersonagemScreen() {
    val personagem = CriarPersonagem().apply {
        nome = "Aragorn"
        raca = "Humano"
        classe = "Guerreiro"
        nivel = 5
        vida = 45
        pontos = 20
        forca = 15
        destreza = 12
        inteligencia = 10
        constituicao = 14
        sabedoria = 10
        carisma = 13
    }
    PersonagemScreen(personagem = personagem)
}
