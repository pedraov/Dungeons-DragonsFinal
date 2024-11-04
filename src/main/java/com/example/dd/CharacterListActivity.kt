package com.example.dd

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dd.ui.theme.DDTheme
import Personagem.CriarPersonagem
import com.example.dd.data.DatabaseHelper
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import com.example.dd.UpdateActivity

class CharacterListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DDTheme {
                CharacterListScreen()
            }
        }
    }
}

@Composable
fun CharacterListScreen() {
    val context = LocalContext.current
    val dbHelper = DatabaseHelper(context)
    val characters = remember { mutableStateListOf<CriarPersonagem>() }

    LaunchedEffect(Unit) {
        characters.clear()
        characters.addAll(dbHelper.getAllCharacters())
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Button(onClick = { (context as CharacterListActivity).finish() }) {
            Text("Voltar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (characters.isEmpty()) {
            Toast.makeText(context, "Nenhum personagem encontrado.", Toast.LENGTH_SHORT).show()
        } else {
            CharacterList(characters, dbHelper)
        }
    }
}


@Composable
fun CharacterList(characters: List<CriarPersonagem>, dbHelper: DatabaseHelper) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(characters) { character ->
            CharacterItem(character, dbHelper)
            Divider()
        }
    }
}

@Composable
fun CharacterItem(character: CriarPersonagem, dbHelper: DatabaseHelper) {
    val context = LocalContext.current

    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Nome: ${character.nome}", style = MaterialTheme.typography.titleMedium)
        Text(text = "Raça: ${character.raca}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Atributos:", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Força: ${character.forca}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Destreza: ${character.destreza}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Constituição: ${character.constituicao}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Sabedoria: ${character.sabedoria}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Inteligência: ${character.inteligencia}", style = MaterialTheme.typography.bodySmall)
        Text(text = "Carisma: ${character.carisma}", style = MaterialTheme.typography.bodySmall)

        Row {
            Button(onClick = {
                // Inicia a Activity para editar o personagem
                val intent = Intent(context, UpdateActivity::class.java).apply {
                    putExtra("CHARACTER_NAME", character.nome ?: "")
                }
                context.startActivity(intent)
            }) {
                Text("Editar")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = {
                val deletedRows = character.nome?.let { dbHelper.deleteCharacter(it) }
                if (deletedRows != null && deletedRows > 0) {
                    Toast.makeText(context, "${character.nome} removido!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Erro ao remover ${character.nome}.", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Remover")
            }
        }
    }
}
