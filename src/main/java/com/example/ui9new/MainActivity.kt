package com.example.ui9new

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pc_List: ArrayList<Pc> = ArrayList()
        val db = DataBase(applicationContext)

        val url = "https://run.mocky.io/v3/a6d24ef2-c50b-4f63-813b-447f1ac603fa"

         val jsonArrayRequest = JsonArrayRequest(
             Request.Method.GET, url, null,
             { response ->
                 // Parse the JSON response and add data to dataList
                 for (i in 0 until response.length()) {
                     val item = response.getJSONObject(i)

                     val id = item.getInt("id")
                     val name = item.getString("nom")
                     val prix = item.getString("prix")
                     val image = item.getString("image")
                     pc_List.add(Pc(id, name, prix, image))
                     // Insert res into the database
                     for (pc in pc_List) {
                         db.Additem(pc)
                     }
                 }
                 getAdapter(db)

             },
             { error ->
                 Log.e("VolleyError", "Error: ${error.message}", error)
             })
        requestQueue.add(jsonArrayRequest)


        //Operation -----------------------------------
        val add = findViewById<Button>(R.id.add)
        val search = findViewById<Button>(R.id.search)
        val update = findViewById<Button>(R.id.update)
        val del = findViewById<Button>(R.id.delete)

        add.setOnClickListener {
            val id = findViewById<EditText>(R.id.id).text.toString().toInt()
            val nom = findViewById<EditText>(R.id.nom).text.toString()
            val prix = findViewById<EditText>(R.id.prix).text.toString()
            val img = findViewById<EditText>(R.id.image).text.toString()
            if (!db.Id_Exist(id)) {
                db.Additem(Pc(id, nom, prix, img))
            }else
                Toast.makeText(this, "the Item is Exist", Toast.LENGTH_SHORT).show()

            getAdapter(db)
            senddataToAPI()
        }

        update.setOnClickListener {
            val id = findViewById<EditText>(R.id.id).text.toString().toInt()
            if (db.Id_Exist(id)) {
                val nom = findViewById<EditText>(R.id.nom).text.toString()
                val prix = findViewById<EditText>(R.id.prix).text.toString()
                val img = findViewById<EditText>(R.id.image).text.toString()
                db.updateItem(Pc(id, nom, prix, img))
                Toast.makeText(this, "the Item is Updated", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, "the Item is not Exist", Toast.LENGTH_SHORT).show()
            getAdapter(db)
            senddataToAPI()
        }

        del.setOnClickListener {
            val id = findViewById<EditText>(R.id.id).text.toString().toInt()
            if (db.Id_Exist(id)) {
                db.deleteItem(id)
                Toast.makeText(this, "the Item is deleted", Toast.LENGTH_SHORT).show()
            } else
                Toast.makeText(this, "the Item is not Exist", Toast.LENGTH_SHORT).show()

            getAdapter(db)
            senddataToAPI()
        }

        search.setOnClickListener {
            val id = findViewById<EditText>(R.id.id).text.toString().toInt()
            val nom = findViewById<EditText>(R.id.nom)
            val prix = findViewById<EditText>(R.id.prix)
            val img = findViewById<EditText>(R.id.image)
            if (!db.Id_Exist(id)){
                Toast.makeText(this, "the Item is NOT exist", Toast.LENGTH_SHORT).show()
            }else{
                val pc = db.getItem(id)
                nom.setText(pc.name)
                prix.setText(pc.prix)
                img.setText(pc.image)
            }
        }
    }

    //------------------------------------------------------------------------------
    fun getAdapter( db: DataBase){
        val list: RecyclerView = findViewById(R.id.listx)
        list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // get  from the database
        val pcList = db.getAll()
        val List = ArrayList<Pc>()
        for (pc in pcList) {
            List.add(pc)
        }
        val adapter = Adapter(this, List)
        list.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun senddataToAPI() {
        val db = DataBase(applicationContext)
        val pcList = db.getAll()
        val jsonArray = JSONArray()
        // Convert data to JSON format
        for (pc in pcList) {
            val jsonObject = JSONObject()
            jsonObject.put("id", pc.id)
            jsonObject.put("name", pc.name)
            jsonObject.put("prix", pc.prix)
            jsonObject.put("image", pc.image)
            jsonArray.put(jsonObject)
        }

        // Send data to API using Volley
        val url = "https://run.mocky.io/v3/a6d24ef2-c50b-4f63-813b-447f1ac603fa"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.POST, url, jsonArray,
            Response.Listener { response ->
            },
            Response.ErrorListener { error ->
                Log.e("VolleyError", "Error: ${error.message}", error)
            })
        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonArrayRequest)
    }

    fun onItemClick(pc: Pc) {
        Toast.makeText(this, "Clicked on item: ${pc.name}", Toast.LENGTH_SHORT).show()
    }
}

