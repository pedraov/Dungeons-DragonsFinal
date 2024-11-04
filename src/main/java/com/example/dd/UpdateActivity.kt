package com.example.dd

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.dd.databinding.ActivityUpdateBinding
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.dd.data.DatabaseHelper

class UpdateActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var characterName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            dbHelper = DatabaseHelper(this)

            // Verifica se o nome foi recebido corretamente
            characterName = intent.getStringExtra("CHARACTER_NAME") ?: ""

            if (characterName.isEmpty()) {
                Toast.makeText(this, "Erro: Nome do personagem não encontrado", Toast.LENGTH_LONG).show()
                finish() // Finaliza a Activity caso o nome não seja encontrado
                return@setContent
            }

            CharacterUpdateScreen(
                characterName = characterName,
                dbHelper = dbHelper,
                onUpdateSuccess = {
                    Toast.makeText(this, "Personagem atualizado!", Toast.LENGTH_SHORT).show()
                    finish()
                },
                onUpdateError = { error ->
                    Toast.makeText(this, "Erro ao atualizar personagem: $error", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    fun updateCharacter(
        originalName: String,
        newName: String,
        race: String,
        strength: Int,
        intelligence: Int,
        dexterity: Int,
        wisdom: Int,
        constitution: Int,
        charisma: Int
    ): Boolean {
        return try {
            dbHelper.updateCharacter(originalName, newName, race, strength, intelligence, dexterity, wisdom, constitution, charisma)
            true
        } catch (e: Exception) {
            false
        }
    }
}

@Composable
fun CharacterUpdateScreen(
    characterName: String,
    dbHelper: DatabaseHelper,
    onUpdateSuccess: () -> Unit,
    onUpdateError: (String) -> Unit
) {
    var nome by remember { mutableStateOf(characterName) }
    var raca by remember { mutableStateOf("") }
    var forca by remember { mutableStateOf(0) }
    var inteligencia by remember { mutableStateOf(0) }
    var destreza by remember { mutableStateOf(0) }
    var sabedoria by remember { mutableStateOf(0) }
    var constituicao by remember { mutableStateOf(0) }
    var carisma by remember { mutableStateOf(0) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Editando: $characterName", style = MaterialTheme.typography.headlineMedium)

        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome") }
        )

        // Campos de entrada para atributos
        TextField(
            value = raca,
            onValueChange = { raca = it },
            label = { Text("Raça") }
        )

        TextField(
            value = forca.toString(),
            onValueChange = { forca = it.toIntOrNull() ?: 0 },
            label = { Text("Força") }
        )

        TextField(
            value = inteligencia.toString(),
            onValueChange = { inteligencia = it.toIntOrNull() ?: 0 },
            label = { Text("Inteligência") }
        )

        TextField(
            value = destreza.toString(),
            onValueChange = { destreza = it.toIntOrNull() ?: 0 },
            label = { Text("Destreza") }
        )

        TextField(
            value = sabedoria.toString(),
            onValueChange = { sabedoria = it.toIntOrNull() ?: 0 },
            label = { Text("Sabedoria") }
        )

        TextField(
            value = constituicao.toString(),
            onValueChange = { constituicao = it.toIntOrNull() ?: 0 },
            label = { Text("Constituição") }
        )

        TextField(
            value = carisma.toString(),
            onValueChange = { carisma = it.toIntOrNull() ?: 0 },
            label = { Text("Carisma") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val success = (context as UpdateActivity).updateCharacter(
                characterName, nome, raca, forca, inteligencia, destreza, sabedoria, constituicao, carisma
            )
            if (success) {
                onUpdateSuccess()
            } else {
                onUpdateError("Falha ao atualizar personagem.")
            }
        }) {
            Text("Atualizar Personagem")
        }
    }
}




