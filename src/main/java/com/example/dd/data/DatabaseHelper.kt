package com.example.dd.data

import Personagem.CriarPersonagem
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.Cursor

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "character.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "characters"

        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_RACE = "race"
        private const val COLUMN_CLASS = "class"
        private const val COLUMN_LEVEL = "level"
        private const val COLUMN_HEALTH = "health"
        private const val COLUMN_POINTS = "points"

        private const val COLUMN_STRENGTH = "strength"
        private const val COLUMN_DEXTERITY = "dexterity"
        private const val COLUMN_INTELLIGENCE = "intelligence"
        private const val COLUMN_CONSTITUTION = "constitution"
        private const val COLUMN_WISDOM = "wisdom"
        private const val COLUMN_CHARISMA = "charisma"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_RACE TEXT,
                $COLUMN_CLASS TEXT,
                $COLUMN_LEVEL INTEGER,
                $COLUMN_HEALTH INTEGER,
                $COLUMN_POINTS INTEGER,
                $COLUMN_STRENGTH INTEGER,
                $COLUMN_DEXTERITY INTEGER,
                $COLUMN_INTELLIGENCE INTEGER,
                $COLUMN_CONSTITUTION INTEGER,
                $COLUMN_WISDOM INTEGER,
                $COLUMN_CHARISMA INTEGER
            )
        """
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Função para inserir um novo personagem
    fun insertCharacter(character: CriarPersonagem): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, character.nome)
            put(COLUMN_RACE, character.raca)
            put(COLUMN_CLASS, character.classe)
            put(COLUMN_LEVEL, character.nivel)
            put(COLUMN_HEALTH, character.vida)
            put(COLUMN_POINTS, character.pontos)
            put(COLUMN_STRENGTH, character.forca)
            put(COLUMN_DEXTERITY, character.destreza)
            put(COLUMN_INTELLIGENCE, character.inteligencia)
            put(COLUMN_CONSTITUTION, character.constituicao)
            put(COLUMN_WISDOM, character.sabedoria)
            put(COLUMN_CHARISMA, character.carisma)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    // Função para obter todos os personagens
    fun getAllCharacters(): List<CriarPersonagem> {
        val db = readableDatabase
        val cursor: Cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        val characters = mutableListOf<CriarPersonagem>()

        with(cursor) {
            while (moveToNext()) {
                val character = CriarPersonagem().apply {
                    nome = getString(getColumnIndexOrThrow(COLUMN_NAME))
                    raca = getString(getColumnIndexOrThrow(COLUMN_RACE))
                    classe = getString(getColumnIndexOrThrow(COLUMN_CLASS))
                    nivel = getInt(getColumnIndexOrThrow(COLUMN_LEVEL))
                    vida = getInt(getColumnIndexOrThrow(COLUMN_HEALTH))
                    pontos = getInt(getColumnIndexOrThrow(COLUMN_POINTS))
                    forca = getInt(getColumnIndexOrThrow(COLUMN_STRENGTH))
                    destreza = getInt(getColumnIndexOrThrow(COLUMN_DEXTERITY))
                    inteligencia = getInt(getColumnIndexOrThrow(COLUMN_INTELLIGENCE))
                    constituicao = getInt(getColumnIndexOrThrow(COLUMN_CONSTITUTION))
                    sabedoria = getInt(getColumnIndexOrThrow(COLUMN_WISDOM))
                    carisma = getInt(getColumnIndexOrThrow(COLUMN_CHARISMA))
                }
                characters.add(character)
            }
        }
        cursor.close()
        return characters
    }

    // Função para atualizar um personagem
    fun updateCharacter(originalName: String, nome: String, raca: String, forca: Int, inteligencia: Int, destreza: Int, sabedoria: Int, constituicao: Int, carisma: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", nome)
            put("race", raca)
            put("strength", forca)
            put("intelligence", inteligencia)
            put("dexterity", destreza)
            put("wisdom", sabedoria)
            put("constitution", constituicao)
            put("charisma", carisma)
        }

        db.update("characters", values, "name = ?", arrayOf(originalName))
        db.close()
    }

    // Função para deletar um personagem
    fun deleteCharacter(nome: String): Int {
        val db = writableDatabase
        val selection = "$COLUMN_NAME = ?"
        val selectionArgs = arrayOf(nome)
        return db.delete(TABLE_NAME, selection, selectionArgs)
    }

    @SuppressLint("Range")
    fun getCharacter(characterName: String): CriarPersonagem? {
        val db = readableDatabase
        val cursor: Cursor? = db.query(
            "personagens", // Nome da tabela
            null, // Seleciona todas as colunas
            "name = ?", // Condição WHERE
            arrayOf(characterName), // Argumentos da condição
            null, // Não agrupar as linhas
            null, // Não filtrar por grupos
            null // Não ordenar
        )

        cursor?.let {
            if (it.moveToFirst()) {
                // Pega os valores das colunas, conforme a estrutura da sua classe CriarPersonagem
                val nome = it.getString(it.getColumnIndex("nome"))
                val raca = it.getString(it.getColumnIndex("raca"))
                val classe = it.getString(it.getColumnIndex("classe"))
                val nivel = it.getInt(it.getColumnIndex("nivel"))
                val vida = it.getInt(it.getColumnIndex("vida"))
                val pontos = it.getInt(it.getColumnIndex("pontos"))
                val forca = it.getInt(it.getColumnIndex("forca"))
                val destreza = it.getInt(it.getColumnIndex("destreza"))
                val inteligencia = it.getInt(it.getColumnIndex("inteligencia"))
                val constituicao = it.getInt(it.getColumnIndex("constituicao"))
                val sabedoria = it.getInt(it.getColumnIndex("sabedoria"))
                val carisma = it.getInt(it.getColumnIndex("carisma"))

                // Retorna uma nova instância de CriarPersonagem
                val personagem = CriarPersonagem().apply {
                    EscolherNome(nome)
                    this.raca = raca
                    this.classe = classe
                    this.nivel = nivel
                    this.vida = vida
                    this.pontos = pontos
                    this.forca = forca
                    this.destreza = destreza
                    this.inteligencia = inteligencia
                    this.constituicao = constituicao
                    this.sabedoria = sabedoria
                    this.carisma = carisma
                }
                return personagem
            }
        }

        // Fecha o cursor e o banco de dados
        cursor?.close()
        db.close()
        return null // Retorna null se o personagem não for encontrado
    }


}
