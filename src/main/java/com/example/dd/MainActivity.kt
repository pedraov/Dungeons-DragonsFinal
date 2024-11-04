package com.example.dd

import Personagem.CriarPersonagem
import Personagem.Raças.*
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dd.ui.theme.DDTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DDTheme {
                MainScreen()
            }
        }
    }
}

// Função Composable principal
@Composable
fun MainScreen() {
    val selectedRace = remember { mutableStateOf("") }  // Estado para a raça selecionada
    val characterName = remember { mutableStateOf("") }  // Estado para o nome do personagem
    val p: CriarPersonagem = CriarPersonagem()
    val focusManager = LocalFocusManager.current  // Gerenciador de foco
    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val intent = Intent(context, CharacterListActivity::class.java)
                context.startActivity(intent)
            }) {
                // Texto "Personagens Salvos" no botão flutuante
                Text("Personagens Salvos")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EscolherNome(characterName)  // Passa o estado do nome

                RaceSelectionScreen(selectedRace, focusManager)  // Seção de seleção da raça

                Button(
                    onClick = {
                        if (characterName.value.isNotEmpty() && selectedRace.value.isNotEmpty()) {
                            // Preenche os dados do personagem
                            p.nome = characterName.value
                            p.raca = selectedRace.value
                            escolherRacaEBonus(selectedRace.value, p)

                            // Cria um Intent para iniciar a CharacterListActivity
                            val intent = Intent(context, SecondActivity::class.java).apply {
                                putExtra("personagem", p)
                            }
                            context.startActivity(intent)
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Salvar Nome e Raça")
                }
            }
        }
    }
}

@Composable
fun EscolherNome(characterName: MutableState<String>) {
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Escolha seu nome") },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        characterName.value = text.trim()
                    }
                }
        )
    }
}

@Composable
fun RaceSelectionScreen(selectedRace: MutableState<String>, focusManager: FocusManager) {
    val races = listOf(
        "Humano", "Elfo Alto", "Elfo Drow", "Elfo da Floresta",
        "Anão da Colina", "Anão da Montanha", "Draconato",
        "Gnomo da Floresta", "Gnomo das Rochas", "Halfling Leve",
        "Halfling Robusto", "Meio Elfo", "Meio Orc", "Tiefling"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Escolha a raça do personagem", style = MaterialTheme.typography.titleLarge)

        races.forEach { race ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedRace.value = race
                        focusManager.clearFocus()
                    }
            ) {
                RadioButton(
                    selected = (selectedRace.value == race),
                    onClick = {
                        selectedRace.value = race
                        focusManager.clearFocus()
                    }
                )
                Text(text = race)
            }
        }
    }
}

// Função para aplicar a raça e bônus
fun escolherRacaEBonus(race: String, personagem: CriarPersonagem) {
    val racaStrategy = when (race) {
        "Humano" -> Humano()
        "Elfo Alto" -> ElfoAlto()
        "Elfo Drow" -> ElfoDrow()
        "Elfo da Floresta" -> ElfoFloresta()
        "Anão da Colina" -> AnãoColina()
        "Anão da Montanha" -> AnãoMontanha()
        "Draconato" -> Draconato()
        "Gnomo da Floresta" -> GnomoFloresta()
        "Gnomo das Rochas" -> GnomoRochas()
        "Halfling Leve" -> HalflingLeve()
        "Halfling Robusto" -> HalflingRobusto()
        "Meio Elfo" -> MeioElfo()
        "Meio Orc" -> Orc()
        "Tiefling" -> Tiefling()
        else -> throw IllegalArgumentException("Raça desconhecida")
    }

    racaStrategy.escolherRaça(personagem)
    racaStrategy.bonusRaca(personagem)
}

@Composable
@Preview(showBackground = true)
fun DefaultPreview() {
    MainScreen()
}
