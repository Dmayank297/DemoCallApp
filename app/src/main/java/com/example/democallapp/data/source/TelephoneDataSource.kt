package com.example.democallapp.data.source

import android.content.Context
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import com.example.democallapp.domain.model.RealCallState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TelephonyDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val telephonyManager: TelephonyManager =
        context.getSystemService(TelephonyManager::class.java)

    fun observeCallState(): Flow<RealCallState> = callbackFlow {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val callback = object : TelephonyCallback(), TelephonyCallback.CallStateListener {
                override fun onCallStateChanged(state: Int) {
                    trySend(mapToRealCallState(state))
                }
            }
            telephonyManager.registerTelephonyCallback(context.mainExecutor, callback)
            awaitClose { telephonyManager.unregisterTelephonyCallback(callback) }
        } else {
            @Suppress("DEPRECATION")
            val listener = object : PhoneStateListener() {
                @Deprecated("Deprecated in Java")
                override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                    trySend(mapToRealCallState(state))
                }
            }
            @Suppress("DEPRECATION")
            telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE)
            awaitClose {
                @Suppress("DEPRECATION")
                telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE)
            }
        }
    }

    private fun mapToRealCallState(state: Int): RealCallState = when (state) {
        TelephonyManager.CALL_STATE_OFFHOOK -> RealCallState.Active
        TelephonyManager.CALL_STATE_RINGING -> RealCallState.Ringing
        else -> RealCallState.Idle
    }
}