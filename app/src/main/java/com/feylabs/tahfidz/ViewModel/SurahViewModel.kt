package com.feylabs.tahfidz.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SurahViewModel : ViewModel() {

    private var s = mutableListOf<String>()
    private var a = mutableListOf<Int>()

    val surahList = MutableLiveData<MutableList<String>>()
    val status = MutableLiveData<Boolean>()
    val length = MutableLiveData<String>()

    fun addSurat() {
        s.clear()
        a.clear()
        s.add("Al-Fatihah");
        a.add(7)
        s.add("Al-Baqoroh");
        a.add(286)
        s.add("Ali-Imran");
        a.add(200)
        s.add("An-Nisaa");
        a.add(176)
        s.add("Al-Maa-idah");
        a.add(120)
        s.add("Al-An'aam.165.");
        a.add(165)
        s.add("Al-A'raaf.206.");
        a.add(206)
        s.add("Al-Anfaal.75.");
        a.add(75)
        s.add("At-Taubah.129.");
        a.add(129)
        s.add("Yunus.109.");
        a.add(109)
        s.add("Hud.123.");
        a.add(123)
        s.add("Yusuf.111.");
        a.add(111)
        s.add("Ar-Ra'd.43.");
        a.add(43)
        s.add("Ibrahim.52.");
        a.add(52)
        s.add("Al-Hijr.99.");
        a.add(99)
        s.add("An-Nahl.128.");
        a.add(128)
        s.add("Al-Isra.111.");
        a.add(111)
        s.add("Al-Kahfi.110.");
        a.add(110)
        s.add("Maryam.98.");
        a.add(98)
        s.add("Thaha.135.");
        a.add(135)
        s.add("Al-Anbiyaa.112.");
        a.add(112)
        s.add("Al-Hajj.112.");
        a.add(112)
        s.add("Al-Mu'minuun.118.");
        a.add(118)
        s.add("An-Nuur.64.");
        a.add(64)
        s.add("Al-Furqaan.77.");
        a.add(77)
        s.add("Asy-Syu'araa.227.");
        a.add(227)
        s.add("An-Naml.93.");
        a.add(93)
        s.add("Al-Qashash.88.");
        a.add(88)
        s.add("Al-Ankabut.69.");
        a.add(69)
        s.add("Ar-Ruum.60.");
        a.add(60)
        s.add("Luqman.34.");
        a.add(34)
        s.add("As-Sajadah.30.");
        a.add(30)
        s.add("Al-Ahzab.73.");
        a.add(73)
        s.add("Saba.54.");
        a.add(54)
        s.add("Faathir.45.");
        a.add(45)
        s.add("Yaa Siin.83.");a.add(83)
        s.add("Ash-Shaffaat.182.");
        a.add(182)
        s.add("Shaad.88.");
        a.add(88)
        s.add("Az-Zumar.75.");
        a.add(75)
        s.add("Al-Mu'min.85.");
        a.add(85)
        s.add("FushShilat.54.");
        a.add(54)
        s.add("Asy-Syuura.53.");
        a.add(53)
        s.add("Az-Zukhruf89.");
        a.add(89)
        s.add("Ad-Dukhaan.59.");
        a.add(59)
        s.add("Al-Jaatsiyah.37.");
        a.add(37)
        s.add("Al-Ahqaaf.35.");
        a.add(35)
        s.add("Muhammad.38.");
        a.add(38)
        s.add("Al-Fath.29.");
        a.add(29)
        s.add("Al-Hujurat.18.");
        a.add(18)
        s.add("Qaaf.45.");
        a.add(45)
        s.add("Adz-Dzaariyaat.60.");
        a.add(60)
        s.add("Ath-Thuur.49.");
        a.add(49)
        s.add("An-Najm.62.");
        a.add(62)
        s.add("Al-Qamar.55.");
        a.add(55)
        s.add("Ar-Rahmaan.78.");
        a.add(78)
        s.add("Al-Waaqiah.96.");
        a.add(96)
        s.add("Al-Hadiid.29.");
        a.add(29)
        s.add("Al-Mujaadilah.22.");
        a.add(22)
        s.add("Al-Hasyr.24.");
        a.add(24)
        s.add("Al-Mumtahanah.13.");
        a.add(13)
        s.add("Ash-Shaff.14.");
        a.add(14)
        s.add("Al-Jumu'ah.11.");
        a.add(11)
        s.add("Al-Munaafiquun.11.");
        a.add(11)
        s.add("At-Taghaabun.18.");
        a.add(18)
        s.add("Ath-Thalaaq.12.");
        a.add(12)
        s.add("At-Tahrim.12.");
        a.add(12)
        s.add("Al-Mulk.30.");
        a.add(30)
        s.add("Al-Qalam.52.");
        a.add(52)
        s.add("Al-Haaqqah.52.");
        a.add(52)
        s.add("Al-Ma'aarij.44.");
        a.add(44)
        s.add("Nuh.28.");
        a.add(28)
        s.add("Al-Jin.28.");
        a.add(28)
        s.add("Al-Muzzammil.20.");
        a.add(20)
        s.add("Al-Muddatstsir.56.");
        a.add(56)
        s.add("Al-Qiyaamah.40.");
        a.add(40)
        s.add("Al-Insaan.31.");
        a.add(31)
        s.add("Al-Mursalaat.50.");
        a.add(50)
        s.add("An-Naba.40.");
        a.add(40)
        s.add("An-Naazi'aat.46.");
        a.add(46)
        s.add("'Abasa.42.");
        a.add(42)
        s.add("At-Takwir.29.");
        a.add(29)
        s.add("Al-Infithar.19.");
        a.add(19)
        s.add("Al-Muthaffifiin.36.");
        a.add(36)
        s.add("Al-Insyiqaaq.25.");
        a.add(25)
        s.add("Al-Buruuj.22.");
        a.add(22)
        s.add("Ath-Thaariq.17.");
        a.add(17)
        s.add("Al-A'laa.19.");
        a.add(19)
        s.add("Al-Ghaasyiyah.26.");
        a.add(26)
        s.add("Al-Fajr.30.");
        a.add(30)
        s.add("Al-Balad.20.");
        a.add(20)
        s.add("Asy-Syams.15.");
        a.add(15)
        s.add("Al-Lail.21.");
        a.add(21)
        s.add("Ad-Dhuhaa.11.");
        a.add(11)
        s.add("Alam-Nasyrah.8.");
        a.add(8)
        s.add("At-Tiin.8.");
        a.add(8)
        s.add("Al-'Alaq.19.");
        a.add(19)
        s.add("Al-Qadar.5.");
        a.add(5)
        s.add("Al-Bayyinah.8.");
        a.add(8)
        s.add("Al-Zalzalah.8.");
        a.add(8)
        s.add("Al-'Aadiyaat.11.");
        a.add(11)
        s.add("Al-Qaari'ah.11.");
        a.add(11)
        s.add("At-Takaatsur.8.");
        a.add(8)
        s.add("Al-'Ashr.3.");
        a.add(3)
        s.add("Al-Humazah.9.");
        a.add(9)
        s.add("Al-Fiil.5.");
        a.add(5)
        s.add("Al-Quraisy.4.");
        a.add(4)
        s.add("Al-Maa'un.7.");
        a.add(7)
        s.add("Al-Kautsar.3.");
        a.add(3)
        s.add("Al-Kaafiruun.6.");
        a.add(3)
        s.add("An-Nashr.3.");
        a.add(3)
        s.add("Al-Lahab.5.");
        a.add(5)
        s.add("Al-Ikhlash.4.");
        a.add(4)
        s.add("Al-Falaq.5.");
        a.add(5)
        s.add("An-Naas.6.");
        a.add(6)

        surahList.postValue(s)
        if (s.size==144){
            status.postValue(true)
        }else{
            length.postValue("Panjang Data : "+s.size)
            status.postValue(false)
        }

    }

}