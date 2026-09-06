package com.bharatloan.app

import android.Manifest
import android.app.*
import android.os.*
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.Locale
import kotlin.math.pow

class MainActivity : Activity() {
    private lateinit var content: LinearLayout
    private fun tv(t:String,s:Float=16f)=TextView(this).apply{text=t;textSize=s;setPadding(0,10,0,10)}
    private fun btn(t:String)=Button(this).apply{text=t}
    override fun onCreate(b:Bundle?){super.onCreate(b); home()}
    private fun base(title:String){
        val scroll=ScrollView(this)
        content=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL;setPadding(28,28,28,28)}
        scroll.addView(content); setContentView(scroll); content.addView(tv(title,28f))
    }
    private fun home(){
        base("Bharat Loan")
        content.addView(tv("Fast & simple loan application",18f))
        val amount=EditText(this).apply{hint="Loan amount (₹)";inputType=2}; content.addView(amount)
        val tenure=EditText(this).apply{hint="Tenure (months)";inputType=2}; content.addView(tenure)
        val emi=tv("Estimated EMI: —",18f);content.addView(emi)
        content.addView(btn("Calculate EMI").apply{setOnClickListener{
            val p=amount.text.toString().toDoubleOrNull()?:0.0; val n=tenure.text.toString().toIntOrNull()?:0; val r=.18/12
            val e=if(p>0&&n>0)p*r*(1+r).pow(n)/((1+r).pow(n)-1) else 0.0
            emi.text=if(e>0)"Estimated EMI: ₹"+String.format(Locale.US,"%.2f",e) else "Estimated EMI: —"
        }})
        content.addView(btn("Apply for Loan").apply{setOnClickListener{application()}})
        content.addView(btn("My Loan / Status").apply{setOnClickListener{status()}})
        content.addView(btn("Repayment").apply{setOnClickListener{repayment()}})
        content.addView(btn("KYC & Documents").apply{setOnClickListener{kyc()}})
        content.addView(btn("Profile").apply{setOnClickListener{profile()}})
        content.addView(btn("Admin Demo").apply{setOnClickListener{admin()}})
        content.addView(tv("Privacy: permissions are requested only when needed. Prototype only; no real loan is disbursed.",14f))
    }
    private fun application(){
        base("Loan Application")
        listOf("Full name","Mobile number","Date of birth","Address","Employment / business","Monthly income","Requested loan amount").forEach{content.addView(EditText(this).apply{hint=it})}
        content.addView(btn("Submit Application").apply{setOnClickListener{
            Toast.makeText(this,"Application saved as DEMO / Pending KYC",Toast.LENGTH_LONG).show(); status()
        }})
    }
    private fun kyc(){
        base("KYC & Documents")
        content.addView(tv("Select documents yourself. The app does not silently scan your gallery."))
        content.addView(btn("Select ID / Address Photo").apply{setOnClickListener{
            startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).apply{type="image/*";addCategory(Intent.CATEGORY_OPENABLE)},20)
        }})
        content.addView(btn("Allow Contacts (Optional)").apply{setOnClickListener{
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.READ_CONTACTS),30)
            else Toast.makeText(this,"Contacts permission already granted",Toast.LENGTH_SHORT).show()
        }})
        content.addView(tv("Only use customer contacts when there is a legitimate, disclosed feature and appropriate consent."))
    }
    private fun status(){base("My Loan / Status");content.addView(tv("Application #BL-DEMO-001",18f));content.addView(tv("Status: Pending KYC"));content.addView(tv("Requested amount: ₹50,000"));content.addView(tv("No real disbursement is connected."))}
    private fun repayment(){base("Repayment");content.addView(tv("No live loan found",20f));content.addView(tv("Payment gateway is not connected in this prototype."));content.addView(btn("Payment (Demo)").apply{setOnClickListener{Toast.makeText(this,"Demo only",Toast.LENGTH_SHORT).show()}})}
    private fun profile(){base("Profile");content.addView(tv("Customer profile"));listOf("Name","Mobile","Email").forEach{content.addView(EditText(this).apply{hint=it})};content.addView(btn("Save Profile").apply{setOnClickListener{Toast.makeText(this,"Saved (demo)",Toast.LENGTH_SHORT).show()}})}
    private fun admin(){base("Admin Dashboard (Demo)");content.addView(tv("Applications: 1"));content.addView(tv("BL-DEMO-001 — Pending KYC"));content.addView(btn("Approve (Demo)").apply{setOnClickListener{Toast.makeText(this,"Demo approval only",Toast.LENGTH_SHORT).show()}});content.addView(btn("Reject (Demo)").apply{setOnClickListener{Toast.makeText(this,"Demo rejection only",Toast.LENGTH_SHORT).show()}})}
}