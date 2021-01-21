package com.grevi.aywapet.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun snackBar(view : View, msg : String) : Snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)