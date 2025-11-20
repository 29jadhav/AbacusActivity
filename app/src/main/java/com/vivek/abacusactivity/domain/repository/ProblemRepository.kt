package com.vivek.abacusactivity.domain.repository

import com.vivek.abacusactivity.domain.model.Problem
import kotlin.random.Random

interface ProblemRepository {
    fun generateProblem(random: Random = Random): Problem
}