package com.example.ejemplobusqueda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ejemplobusqueda.ui.theme.EjemploBusquedaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EjemploBusquedaTheme {
                SearchExample()
            }
        }
    }
}

@Composable
fun SearchExample() {
    val fruits = listOf<String>("Manzana", "Pera", "Pátano", "Sandía", "Mango", "Melón")

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var filterFruits = remember(key1 = searchQuery.text) {
            if (searchQuery.text.isBlank()) fruits                       /*Este ignore case tienes que modificarlo*/
            else fruits.filter { fruits -> fruits.contains(searchQuery.text, ignoreCase = false) }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(15.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {searchQuery=it},
            label = {Text("Buscar fruta")},
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(15.dp))

        LazyColumn{ items(filterFruits){fruit ->
            Text(
                text = fruit,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Divider()
        } }
    }

}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EjemploBusquedaTheme {
        SearchExample()
    }
}