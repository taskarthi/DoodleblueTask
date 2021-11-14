package com.doodlebluetask

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.doodlebluetask.adapter.MyCustomAdapter
import com.doodlebluetask.broadcast.PhoneStateReceiver
import com.doodlebluetask.databinding.ActivityReadcontactBinding
import com.doodlebluetask.model.ContactsInfo
import com.doodlebluetask.viewmodel.ContactsViewModel
import kotlinx.android.synthetic.main.activity_readcontact.*

class ReadContactActivity : AppCompatActivity() {

    private var dataAdapter: MyCustomAdapter? = null
    private var phReceiver: PhoneStateReceiver? = null
    private var binding: ActivityReadcontactBinding? = null
    private lateinit var contactsViewModel: ContactsViewModel
    private var contacts = ArrayList<ContactsInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        contactsViewModel =
            ViewModelProvider(this).get(ContactsViewModel::class.java)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_readcontact)
        requestCalls()
        phReceiver = PhoneStateReceiver()
        contactsViewModel.contactsInfoLiveData.observe(this, androidx.lifecycle.Observer {
            contacts = it
            dataAdapter = MyCustomAdapter(
                applicationContext, R.layout.contact_info,
                it.distinctBy {
                    it.displayName
                }.sortedBy { it.displayName }
            )
            lstContacts!!.adapter = dataAdapter
            lstContacts!!.setOnItemClickListener { parent, view, position, id ->
                val dataModel: ContactsInfo = it[position]
                dataModel.checked = !dataModel.checked
                dataAdapter!!.notifyDataSetChanged()
            }
        })
    }

    private fun requestCalls() {

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_PHONE_STATE,
            ),
            PERMISSIONS_REQUEST_READ_CONTACTS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                contactsViewModel.getContacts(contentResolver)
            } else {
                Toast.makeText(
                    this,
                    "Until you grant the permission, we canot display the names",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        const val PERMISSIONS_REQUEST_READ_CONTACTS = 1
    }

}