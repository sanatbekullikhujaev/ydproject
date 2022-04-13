package com.usc0der.ydprojectnew.api.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usc0der.ydprojectnew.api.repository.Repository
import com.usc0der.ydprojectnew.model.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(private val repository: Repository) : ViewModel() {
    private val _playerSateFlow = MutableStateFlow<PlayerUiSate>(PlayerUiSate.Loading)
    val playerSateFlow: StateFlow<PlayerUiSate> = _playerSateFlow
    fun initLesson(id: Int) = viewModelScope.launch {
        try {
            val response = repository.getLesson(id)
            if (response.success == true) {
                _playerSateFlow.value = PlayerUiSate.Success(response, true)
            } else if (response.errors != null) {
//            val error = "Dasturimizdan 24 soat bepul foydalanish vaqti tugadi endilikda siz pullik obunaga otishingiz lozim \r\n\r\n\r\npullik obunaga o'tish narxlari:\r\n\r\n3 oylik obunaga (80.000 so'm UZB, 800Rubl RUS)\r\n \r\n4 oylik obunaga (100.000 so'm, 1000Rubl RUS) \r\n6 oylik obunaga (150.000 so'm, 1300Rubl RUS) \r\n1 yiliik obunaga (300.000 so'm, 2200Rubl RUS). \r\n\r\nRublda to'lov qilubchilar uchun chegirmalar  ham mavjud. \r\n6 oylikka 10% \r\n1 yillika   20% skidka bor.\r\n\r\nSkidkadan foydalanish uchun, shunchaki YouTube dagi \"YangiDavr\" kanalimizga obuna bo'ling va rasmga olib ko'rsatilgan raqamga \"Telegram\" dan yuboring.\r\n\r\nBiz bilan aloqa +99899-792-56-38\r\n\r\nTo'lovlar Payme, Click, Sberbank visa, Master card, qiwi, Webmoney orqali qabul qilinadi.\r\n "
                _playerSateFlow.value = PlayerUiSate.Error(response.errors ?: "")
            }
        } catch (e: Exception) {
//            val error = "Dasturimizdan 24 soat bepul foydalanish vaqti tugadi endilikda siz pullik obunaga otishingiz lozim \r\n\r\n\r\npullik obunaga o'tish narxlari:\r\n\r\n3 oylik obunaga (80.000 so'm UZB, 800Rubl RUS)\r\n \r\n4 oylik obunaga (100.000 so'm, 1000Rubl RUS) \r\n6 oylik obunaga (150.000 so'm, 1300Rubl RUS) \r\n1 yiliik obunaga (300.000 so'm, 2200Rubl RUS). \r\n\r\nRublda to'lov qilubchilar uchun chegirmalar  ham mavjud. \r\n6 oylikka 10% \r\n1 yillika   20% skidka bor.\r\n\r\nSkidkadan foydalanish uchun, shunchaki YouTube dagi \"YangiDavr\" kanalimizga obuna bo'ling va rasmga olib ko'rsatilgan raqamga \"Telegram\" dan yuboring.\r\n\r\nBiz bilan aloqa +99899-792-56-38\r\n\r\nTo'lovlar Payme, Click, Sberbank visa, Master card, qiwi, Webmoney orqali qabul qilinadi.\r\n "
            _playerSateFlow.value = PlayerUiSate.Error(e.message ?: "Serverda xatolik yuz berdi")
        }
    }

    sealed class PlayerUiSate {
        data class Success(val model: Player, val status: Boolean) : PlayerUiSate()
        data class Error(val error: String) : PlayerUiSate()
        object Loading : PlayerUiSate()
        object Empty : PlayerUiSate()
    }
}
