package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.ui.theme.CalculatorTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                val viewModel = CalculatorViewModel()
                val systemUiController = rememberSystemUiController()
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Black,
                        darkIcons = false
                    )

                }
                CalculatorUI(
                    viewModel = viewModel,
                )
            }
        }
    }
}

@Composable
fun CalculatorButton(
    symbol: String,
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    textStyle: TextStyle = TextStyle(),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(color)
            .then(modifier)
    ) {
        Text(
            text = symbol,
            style = textStyle,
            fontSize = 16.sp,
            color = Color.White,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalculatorUI(
    viewModel: CalculatorViewModel,
) {
    val expression = viewModel.expression
    val buttonSpacing = 8.dp
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(buttonSpacing),
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth(),
                reverseLayout = true
            ) {
                item {
                    Text(
                        text = expression.value,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp, horizontal = 8.dp),
                        fontWeight = FontWeight.Light,
                        fontSize = 80.sp,
                        color = Color.White,
                        maxLines = 1
                    )
                }
            }
            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "Del/AC",
                    color = Color.Red,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .combinedClickable(
                            onClick = {
                                viewModel.delete()
                            },
                            onLongClick = {
                                viewModel.clear()
                            },
                        )
                )
                CalculatorButton(
                    symbol = "mod",
                    color = Color(0xFF003175),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("%")
                        }
                )
                CalculatorButton(
                    symbol = "(",
                    color = Color(0xFF003175),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("(")
                        }
                )
                CalculatorButton(
                    symbol = ")",
                    color = Color(0xFF003175),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append(")")
                        }
                )

                CalculatorButton(
                    symbol = "÷",
                    color = Color(0xFF003175),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("÷")
                        }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "yˣ",
                    color = Color(0xFF003175),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("^")
                        }
                )
                CalculatorButton(
                    symbol = "√",
                    color = Color(0xFF003175),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("√")
                        }
                )
                CalculatorButton(
                    symbol = "x²",
                    color = Color(0xFF003175),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("x²")
                        }
                )
                CalculatorButton(
                    symbol = "π",
                    color = Color(0xFF003175),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("π")
                        }
                )
                CalculatorButton(
                    symbol = "e",
                    color = Color(0xFF003175),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("e")
                        }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "tan",
                    color = Color(0xFF003175),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("t")
                        }
                )
                CalculatorButton(
                    symbol = "7",
                    color = Color.Gray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("7")
                        }
                )
                CalculatorButton(
                    symbol = "8",
                    color = Color.Gray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("8")
                        }
                )
                CalculatorButton(
                    symbol = "9",
                    color = Color.Gray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("9")
                        }
                )
                CalculatorButton(
                    symbol = "×",
                    color = Color(0xFF003175),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("×")
                        }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "cot",
                    color = Color(0xFF003175),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("c")
                        }
                )
                CalculatorButton(
                    symbol = "4",
                    color = Color.Gray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("4")
                        }
                )
                CalculatorButton(
                    symbol = "5",
                    color = Color.Gray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("5")
                        }
                )
                CalculatorButton(
                    symbol = "6",
                    color = Color.Gray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("6")
                        }
                )
                CalculatorButton(
                    symbol = "-",
                    color = Color(0xFF003175),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("-")
                        }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "sin",
                    color = Color(0xFF003175),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("s")
                        }
                )
                CalculatorButton(
                    symbol = "1",
                    color = Color.Gray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("1")
                        }
                )
                CalculatorButton(
                    symbol = "2",
                    color = Color.Gray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)

                        .clickable {
                            viewModel.append("2")
                        }
                )
                CalculatorButton(
                    symbol = "3",
                    color = Color.Gray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("3")
                        }
                )
                CalculatorButton(
                    symbol = "+",
                    color = Color(0xFF003175),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("+")
                        }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
            ) {
                CalculatorButton(
                    symbol = "cos",
                    color = Color(0xFF003175),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("u")
                        }
                )
                CalculatorButton(
                    symbol = "log",
                    color = Color(0xFF003175),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("log")
                        }
                )
                CalculatorButton(
                    symbol = "0",
                    color = Color.Gray,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.append("0")
                        }
                )
                CalculatorButton(
                    symbol = ". / ,",
                    color = Color(0xFF003175),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .combinedClickable(
                            onClick = {
                                viewModel.append(".")
                            },
                            onLongClick = {
                                viewModel.append(",")
                            },
                        )
                )
                CalculatorButton(
                    symbol = "=",
                    color = Color(0xFF003175),
                    modifier = Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                        .clickable {
                            viewModel.evaluate()
                        }
                )
            }
        }
    }
}