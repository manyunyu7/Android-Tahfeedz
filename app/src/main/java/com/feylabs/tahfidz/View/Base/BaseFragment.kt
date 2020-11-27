package com.feylabs.tahfidz.View.Base

import android.content.pm.PackageManager
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.Util.URL
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_loading_transparent.*
import java.io.File


open class BaseFragment : Fragment() {
    var s = mutableListOf<String>()
    var a = mutableListOf<Int>()

//    tempStudentData["login_type"] = "student"
//    tempStudentData["student_id"] = id
//    tempStudentData["student_name"] = name
//    tempStudentData["student_nisn"] = nisn
//    tempStudentData["student_email"] = email
//    tempStudentData["student_group"] = kelompok
//    tempStudentData["student_contact"] = contact
//    tempStudentData["student_gender"] = gender
//    tempStudentData["created_at"] = created_at
//    tempStudentData["updated_at"] = update_at
//    tempGroupData["id_group"] = groupID
//    tempGroupData["group_name"] = groupName
//    tempGroupData["group_mentor_id"] = mentor_id
//    tempGroupData["group_mentor_name"] = mentor_name
//    tempGroupData["group_mentor_contact"] = mentor_contact

  var xstudent_id=""
  var xstudent_name=""
  var xgroup_id=""

    fun initID(){
        xstudent_id = Preference(requireContext()).getPrefString("student_id").toString()
        xstudent_name = Preference(requireContext()).getPrefString("student_name").toString()
        xgroup_id = Preference(requireContext()).getPrefString("id_group").toString()
        Log.i("deytaInit",xstudent_id)
        Log.i("deytaInit",xstudent_name)
        Log.i("deytaInit",xgroup_id)
    }

    fun String.showToast() {
        Toast.makeText(requireContext(), this, Toast.LENGTH_LONG).show()
    }

    fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                111
            )
    }

    fun downloadPicasso(target: ImageView) {
        checkPermission()
        //Setting Up URL
        val  url = URL.STUDENT_PHOTO + Preference(requireContext()).getPrefString("student_photo")
        Picasso.get()
            .load(url)
            .tag("all-profile")
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .fit()
            .placeholder(R.drawable.empty_profile)
            .error(R.drawable.empty_profile)
            .into(target, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    "Berhasil Memuat Foto Profile"
                }
                override fun onError(e: java.lang.Exception?) {
                    Log.i("fan-url-profile-info",url)
                }
            })

        //CREATE MEDIA DIRECTORY
        val dir = File(Environment.getExternalStorageDirectory().toString() + "/Download/your folder/")
        dir.mkdirs() // creates needed dirs


        val externalCacheFile = File(context?.externalCacheDir, "Lala")
        val bitmap = target.drawable


        val mediaStorageDir = File(context?.externalCacheDir?.absolutePath, "Tahfidz")
        Log.i("path ", context?.externalCacheDir?.absolutePath.toString())
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory")
            }
        }else{
            Log.i("Tahfidz","Success Create Directory")
        }
    }

    fun cfAlert(message:String,bgColor:Int,textColor:Int) {
        val cfAlert = CFAlertDialog.Builder(requireContext())
            .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
            .setTitle(message)
            .setBackgroundColor(bgColor)
            .setTextColor(textColor)
            .setCancelable(true)
            .addButton(
                "OK", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE,
                CFAlertDialog.CFAlertActionAlignment.END
            ) { dialog, which ->
                dialog.dismiss()
            }
        cfAlert.show()
    }

    fun addSurat() {
        s.clear()
        a.clear()
        s.add("Al-Fatihah.7")
        a.add(7)
        s.add("Al-Baqoroh.286")
        a.add(286)
        s.add("Ali-Imran.0")
        a.add(200)
        s.add("An-Nisaa.8")
        a.add(176)
        s.add("Al-Maa-idah.9")
        a.add(120)
        s.add("Al-An'aam.165.")
        a.add(165)
        s.add("Al-A'raaf.206.")
        a.add(206)
        s.add("Al-Anfaal.75.")
        a.add(75)
        s.add("At-Taubah.129.")
        a.add(129)
        s.add("Yunus.109.")
        a.add(109)
        s.add("Hud.123.")
        a.add(123)
        s.add("Yusuf.111.")
        a.add(111)
        s.add("Ar-Ra'd.43.")
        a.add(43)
        s.add("Ibrahim.52.")
        a.add(52)
        s.add("Al-Hijr.99.")
        a.add(99)
        s.add("An-Nahl.128.")
        a.add(128)
        s.add("Al-Isra.111.")
        a.add(111)
        s.add("Al-Kahfi.110.")
        a.add(110)
        s.add("Maryam.98.")
        a.add(98)
        s.add("Thaha.135.")
        a.add(135)
        s.add("Al-Anbiyaa.112.")
        a.add(112)
        s.add("Al-Hajj.112.")
        a.add(112)
        s.add("Al-Mu'minuun.118.")
        a.add(118)
        s.add("An-Nuur.64.")
        a.add(64)
        s.add("Al-Furqaan.77.")
        a.add(77)
        s.add("Asy-Syu'araa.227.")
        a.add(227)
        s.add("An-Naml.93.")
        a.add(93)
        s.add("Al-Qashash.88.")
        a.add(88)
        s.add("Al-Ankabut.69.")
        a.add(69)
        s.add("Ar-Ruum.60.")
        a.add(60)
        s.add("Luqman.34.")
        a.add(34)
        s.add("As-Sajadah.30.")
        a.add(30)
        s.add("Al-Ahzab.73.")
        a.add(73)
        s.add("Saba.54.")
        a.add(54)
        s.add("Faathir.45.")
        a.add(45)
        s.add("Yaa Siin.83.");a.add(83)
        s.add("Ash-Shaffaat.182.")
        a.add(182)
        s.add("Shaad.88.")
        a.add(88)
        s.add("Az-Zumar.75.")
        a.add(75)
        s.add("Al-Mu'min.85.")
        a.add(85)
        s.add("FushShilat.54.")
        a.add(54)
        s.add("Asy-Syuura.53.")
        a.add(53)
        s.add("Az-Zukhruf89.")
        a.add(89)
        s.add("Ad-Dukhaan.59.")
        a.add(59)
        s.add("Al-Jaatsiyah.37.")
        a.add(37)
        s.add("Al-Ahqaaf.35.")
        a.add(35)
        s.add("Muhammad.38.")
        a.add(38)
        s.add("Al-Fath.29.")
        a.add(29)
        s.add("Al-Hujurat.18.")
        a.add(18)
        s.add("Qaaf.45.")
        a.add(45)
        s.add("Adz-Dzaariyaat.60.")
        a.add(60)
        s.add("Ath-Thuur.49.")
        a.add(49)
        s.add("An-Najm.62.")
        a.add(62)
        s.add("Al-Qamar.55.")
        a.add(55)
        s.add("Ar-Rahmaan.78.")
        a.add(78)
        s.add("Al-Waaqiah.96.")
        a.add(96)
        s.add("Al-Hadiid.29.")
        a.add(29)
        s.add("Al-Mujaadilah.22.")
        a.add(22)
        s.add("Al-Hasyr.24.")
        a.add(24)
        s.add("Al-Mumtahanah.13.")
        a.add(13)
        s.add("Ash-Shaff.14.")
        a.add(14)
        s.add("Al-Jumu'ah.11.")
        a.add(11)
        s.add("Al-Munaafiquun.11.")
        a.add(11)
        s.add("At-Taghaabun.18.")
        a.add(18)
        s.add("Ath-Thalaaq.12.")
        a.add(12)
        s.add("At-Tahrim.12.")
        a.add(12)
        s.add("Al-Mulk.30.")
        a.add(30)
        s.add("Al-Qalam.52.")
        a.add(52)
        s.add("Al-Haaqqah.52.")
        a.add(52)
        s.add("Al-Ma'aarij.44.")
        a.add(44)
        s.add("Nuh.28.")
        a.add(28)
        s.add("Al-Jin.28.")
        a.add(28)
        s.add("Al-Muzzammil.20.")
        a.add(20)
        s.add("Al-Muddatstsir.56.")
        a.add(56)
        s.add("Al-Qiyaamah.40.")
        a.add(40)
        s.add("Al-Insaan.31.")
        a.add(31)
        s.add("Al-Mursalaat.50.")
        a.add(50)
        s.add("An-Naba.40.")
        a.add(40)
        s.add("An-Naazi'aat.46.")
        a.add(46)
        s.add("'Abasa.42.")
        a.add(42)
        s.add("At-Takwir.29.")
        a.add(29)
        s.add("Al-Infithar.19.")
        a.add(19)
        s.add("Al-Muthaffifiin.36.")
        a.add(36)
        s.add("Al-Insyiqaaq.25.")
        a.add(25)
        s.add("Al-Buruuj.22.")
        a.add(22)
        s.add("Ath-Thaariq.17.")
        a.add(17)
        s.add("Al-A'laa.19.")
        a.add(19)
        s.add("Al-Ghaasyiyah.26.")
        a.add(26)
        s.add("Al-Fajr.30.")
        a.add(30)
        s.add("Al-Balad.20.")
        a.add(20)
        s.add("Asy-Syams.15.")
        a.add(15)
        s.add("Al-Lail.21.")
        a.add(21)
        s.add("Ad-Dhuhaa.11.")
        a.add(11)
        s.add("Alam-Nasyrah.8.")
        a.add(8)
        s.add("At-Tiin.8.")
        a.add(8)
        s.add("Al-'Alaq.19.")
        a.add(19)
        s.add("Al-Qadar.5.")
        a.add(5)
        s.add("Al-Bayyinah.8.")
        a.add(8)
        s.add("Al-Zalzalah.8.")
        a.add(8)
        s.add("Al-'Aadiyaat.11.")
        a.add(11)
        s.add("Al-Qaari'ah.11.")
        a.add(11)
        s.add("At-Takaatsur.8.")
        a.add(8)
        s.add("Al-'Ashr.3.")
        a.add(3)
        s.add("Al-Humazah.9.")
        a.add(9)
        s.add("Al-Fiil.5.")
        a.add(5)
        s.add("Al-Quraisy.4.")
        a.add(4)
        s.add("Al-Maa'un.7.")
        a.add(7)
        s.add("Al-Kautsar.3.")
        a.add(3)
        s.add("Al-Kaafiruun.6.")
        a.add(3)
        s.add("An-Nashr.3.")
        a.add(3)
        s.add("Al-Lahab.5.")
        a.add(5)
        s.add("Al-Ikhlash.4.")
        a.add(4)
        s.add("Al-Falaq.5.")
        a.add(5)
        s.add("An-Naas.6.")
        a.add(6)

        //Removing Ayah Regex on List s
        for (i in 0 until s.size){
            var a = s[i].toString().split(".").toTypedArray()
            s[i]=a[0].toString()
        }
    }

}