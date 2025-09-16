package com.example.jagruk.ui.screens.module

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.jagruk.data.models.LearningModule
import com.example.jagruk.data.models.ModulePage
import com.example.jagruk.data.models.ModuleStatus
import com.example.jagruk.data.models.PageType
import com.example.jagruk.data.models.Quiz
import com.example.jagruk.data.models.QuizResult
import com.example.jagruk.ui.theme.JagrukTheme
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ModuleScreen(
    moduleId: String,
    viewModel: ModuleViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.moduleUiState.collectAsState()
    val pagerState = rememberPagerState(pageCount = { uiState.module?.pages?.size ?: 0 })
    val scope = rememberCoroutineScope()

    LaunchedEffect(moduleId) {
        viewModel.loadModule(moduleId)
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        uiState.module?.let { module ->
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                topBar = {
                    ModuleHeader(
                        module = module,
                        currentPage = pagerState.currentPage,
                        totalPages = module.pages.size,
                        onNavigateBack = onNavigateBack
                    )
                }
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .padding(it)
                ) { pageIndex ->
                    val page = module.pages[pageIndex]

                    when (page.type) {
                        PageType.CONTENT -> {
                            ContentPage(
                                page = page,
                                onNextPage = {
                                    scope.launch {
                                        if (pageIndex < module.pages.size - 1) {
                                            pagerState.animateScrollToPage(pageIndex + 1)
                                        }
                                    }
                                },
                                onPreviousPage = {
                                    scope.launch {
                                        if (pageIndex > 0) {
                                            pagerState.animateScrollToPage(pageIndex - 1)
                                        }
                                    }
                                },
                                isFirstPage = pageIndex == 0,
                                isLastPage = pageIndex == module.pages.size - 1,
                                onModuleCompleted = {
                                    viewModel.updateModuleStatus(ModuleStatus.COMPLETED)
                                    onNavigateBack()
                                }
                            )
                        }

                        PageType.QUIZ -> {
                            page.quiz?.let { quiz ->
                                QuizPage(
                                    quiz = quiz,
                                    currentQuestionIndex = uiState.currentQuestionIndex,
                                    selectedAnswers = uiState.selectedAnswers,
                                    showResults = uiState.showQuizResults,
                                    quizResult = uiState.quizResult,
                                    onAnswerSelected = viewModel::selectAnswer,
                                    onNextQuestion = viewModel::nextQuestion,
                                    onPreviousQuestion = viewModel::previousQuestion,
                                    onSubmitQuiz = viewModel::submitQuiz,
                                    onRetakeQuiz = viewModel::retakeQuiz,
                                    onNextPage = {
                                        scope.launch {
                                            if (pageIndex < module.pages.size - 1) {
                                                pagerState.animateScrollToPage(pageIndex + 1)
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModuleHeader(
    module: LearningModule,
    currentPage: Int,
    totalPages: Int,
    onNavigateBack: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.fillMaxWidth()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = module.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "${module.estimatedTimeMinutes} min • ${module.difficulty}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }

                Box(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress indicator
            LinearProgressIndicator(
                progress = (currentPage + 1).toFloat() / totalPages.toFloat(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Page ${currentPage + 1} of $totalPages",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun ContentPage(
    page: ModulePage,
    onNextPage: () -> Unit,
    onPreviousPage: () -> Unit,
    onModuleCompleted:()->Unit,
    isFirstPage: Boolean,
    isLastPage: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Text(
                    text = page.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    MarkdownText(markdown = page.content)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (!isFirstPage) {
                OutlinedButton(
                    onClick = onPreviousPage,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Previous")
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.width(16.dp))

            if (!isLastPage) {
                Button(
                    onClick = onNextPage,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Next")
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                Button(
                    onClick = {
                        onModuleCompleted()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Complete")
                }
            }
        }
    }
}

@Composable
private fun QuizPage(
    quiz: Quiz,
    currentQuestionIndex: Int,
    selectedAnswers: Map<String, Int>,
    showResults: Boolean,
    quizResult: QuizResult?,
    onAnswerSelected: (String, Int) -> Unit,
    onNextQuestion: () -> Unit,
    onPreviousQuestion: () -> Unit,
    onSubmitQuiz: () -> Unit,
    onRetakeQuiz: () -> Unit,
    onNextPage: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (showResults && quizResult != null) {
            QuizResultsView(
                quiz = quiz,
                result = quizResult,
                onRetakeQuiz = onRetakeQuiz,
                onNextPage = onNextPage
            )
        } else {
            QuizQuestionView(
                quiz = quiz,
                currentQuestionIndex = currentQuestionIndex,
                selectedAnswers = selectedAnswers,
                onAnswerSelected = onAnswerSelected,
                onNextQuestion = onNextQuestion,
                onPreviousQuestion = onPreviousQuestion,
                onSubmitQuiz = onSubmitQuiz
            )
        }
    }
}

@Composable
private fun QuizQuestionView(
    quiz: Quiz,
    currentQuestionIndex: Int,
    selectedAnswers: Map<String, Int>,
    onAnswerSelected: (String, Int) -> Unit,
    onNextQuestion: () -> Unit,
    onPreviousQuestion: () -> Unit,
    onSubmitQuiz: () -> Unit
) {
    val currentQuestion = quiz.questions[currentQuestionIndex]
    val selectedAnswer = selectedAnswers[currentQuestion.id]

    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = quiz.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "${currentQuestionIndex + 1}/${quiz.questions.size}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = currentQuestion.question,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                lineHeight = MaterialTheme.typography.titleMedium.lineHeight * 1.3
            )

            Spacer(modifier = Modifier.height(20.dp))

            currentQuestion.options.forEachIndexed { index, option ->
                QuizOptionCard(
                    option = option,
                    isSelected = selectedAnswer == index,
                    onOptionSelected = { onAnswerSelected(currentQuestion.id, index) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Quiz navigation buttons
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (currentQuestionIndex > 0) {
            OutlinedButton(
                onClick = onPreviousQuestion,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Previous")
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.width(16.dp))

        if (currentQuestionIndex < quiz.questions.size - 1) {
            Button(
                onClick = onNextQuestion,
                enabled = selectedAnswer != null,
                modifier = Modifier.weight(1f)
            ) {
                Text("Next")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
            }
        } else {
            Button(
                onClick = onSubmitQuiz,
                enabled = selectedAnswers.size == quiz.questions.size,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Submit Quiz")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuizOptionCard(
    option: String,
    isSelected: Boolean,
    onOptionSelected: () -> Unit
) {
    Card(
        onClick = onOptionSelected,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        border = if (isSelected) {
            CardDefaults.outlinedCardBorder().copy(
                brush = SolidColor(MaterialTheme.colorScheme.primary),
                width = 2.dp
            )
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onOptionSelected
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = option,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuizResultsView(
    quiz: Quiz,
    result: QuizResult,
    onRetakeQuiz: () -> Unit,
    onNextPage: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = if (result.passed) Icons.Default.CheckCircle else Icons.Default.Cancel,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = if (result.passed) Color(0xFF4CAF50) else Color(0xFFE57373)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (result.passed) "Quiz Passed!" else "Quiz Not Passed",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = if (result.passed) Color(0xFF4CAF50) else Color(0xFFE57373)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Score: ${result.correctAnswers}/${result.totalQuestions} (${(result.score * 100).toInt()}%)",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (result.passed) {
                    "Congratulations! You've successfully completed this section."
                } else {
                    "You need ${(quiz.passingScore * 100).toInt()}% to pass. Review the content and try again."
                },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (result.passed) Arrangement.End else Arrangement.SpaceBetween
    ) {
        if (!result.passed) {
            OutlinedButton(
                onClick = onRetakeQuiz,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Retake Quiz")
            }
            Spacer(modifier = Modifier.width(16.dp))
        }

        Button(
            onClick = onNextPage,
            enabled = result.passed,
            modifier = Modifier.weight(1f)
        ) {
            Text("Continue")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ModuleScreenPreview() {
    JagrukTheme {
        ContentPage(
            page = ModulePage(
                id = "",
                title = "",
                content = "",
                type = PageType.CONTENT,
                quiz = Quiz(
                    id = "",
                    title = "",
                    questions = emptyList(),
                    passingScore = 100.0,
                ),
            ),
            onNextPage = { },
            onPreviousPage = {},
            isFirstPage = true,
            isLastPage = false,
            onModuleCompleted = {}
        )
    }
}