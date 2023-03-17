package com.example.policyagent.util
import java.io.IOException

class ApiException(message: String) : IOException(message)
class LogOutException(message: String) : IOException(message)
class NoInternetException(message: String) : IOException(message)