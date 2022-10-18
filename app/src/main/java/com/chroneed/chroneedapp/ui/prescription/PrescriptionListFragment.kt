package com.chroneed.chroneedapp.ui.prescription

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.DeletePrescriptionResponse
import com.chroneed.chroneedapp.data.medical.GetPrescriptionListResponse
import com.chroneed.chroneedapp.data.medical.PrescriptionModel
import com.chroneed.chroneedapp.databinding.FragmentPrescriptionListBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.utilities.AlarmReceiver
import com.chroneed.chroneedapp.utilities.CodeManager
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PrescriptionListFragment : Fragment() {
    private var _binding: FragmentPrescriptionListBinding? = null
    private val binding get() = _binding!!

    private val prescriptionAddFragment = PrescriptionAddFragment()
    private var models: MutableList<PrescriptionModel> = mutableListOf()
    private lateinit var modelsAdapter: PrescriptionRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPrescriptionListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        initEvents()
       // cancelAllAlarm()
    }


    private fun initEvents() {
        binding.fragmentPrescriptionListBtnnew.setOnClickListener() {
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(
                    R.id.main_fragment_view_layout,
                    prescriptionAddFragment
                )
                ?.addToBackStack(
                    null
                )
                ?.commit()
        }
        binding.fragmentPrescriptionListBtnback.setOnClickListener() {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun loadData() {
        val tokenba =
            "Bearer " + MySharedPreferences.getUserToken(
                binding.root.context
            )
        var myProgress =
            MyProgressDialog(
                binding.root.context,
                R.raw.lottie_bluetooth_turnedoff
            )
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).getprescriptionbyuser(0, 100)
        apiInterface.enqueue(object : Callback<GetPrescriptionListResponse> {
            override fun onResponse(call: Call<GetPrescriptionListResponse>, response: Response<GetPrescriptionListResponse>) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data.count() > 0) {
                        models.clear()//Todo: need to load in first time
                        models.addAll(
                            response.body()!!.data
                        )
                        modelsAdapter = activity?.let { PrescriptionRecyclerAdapter(models, it) }!!
                        binding.fragmentPrescriptionListRecycler.layoutManager = LinearLayoutManager(activity)
                        binding.fragmentPrescriptionListRecycler.adapter = modelsAdapter
                    }
                }
                myProgress.closeDialog()
            }

            override fun onFailure(
                call: Call<GetPrescriptionListResponse>,
                t: Throwable?
            ) {
                Toast.makeText(
                    context,
                    t?.message,
                    Toast.LENGTH_LONG
                )
                    .show();
                myProgress.closeDialog()
            }
        })

    }


    class PrescriptionRecyclerAdapter constructor(private val items: MutableList<PrescriptionModel>, val activity: FragmentActivity) :
        RecyclerView.Adapter<PrescriptionRecyclerAdapter.viewHolder>() {
        lateinit var context: Context
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): viewHolder {
            context = parent.context
            val inflater =
                LayoutInflater.from(
                    parent.context
                )
                    .inflate(
                        R.layout.card_item_prescription,
                        parent,
                        false
                    )

            return viewHolder(
                inflater
            )
        }

        private fun editItem(holder: viewHolder, @SuppressLint("RecyclerView") position: Int) {

            // activity.putExtraData()
            val fragment = PrescriptionEditFragment()
            val arguments = Bundle()
            val id = items[position].id
            arguments.putString("id", id)
            fragment.arguments = arguments
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_fragment_view_layout, fragment)
                ?.addToBackStack(null)
                ?.commit()

        }

        private fun deleteItem(
            holder: viewHolder,
            @SuppressLint(
                "RecyclerView"
            ) position: Int
        ) {
            val id =
                items[position].id.toString()
            val tokenba =
                "Bearer " + MySharedPreferences.getUserToken(
                    context
                )
            var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
            myProgress.showDialog()

            try {
                var apiInterface = ApiInterface.create(tokenba).deletePrescription(id = id)
                apiInterface.enqueue(object : Callback<DeletePrescriptionResponse> {
                    override fun onResponse(
                        call: Call<DeletePrescriptionResponse>,
                        response: Response<DeletePrescriptionResponse>
                    ) {
                        if (response.body()!!.isSuccess == true) {
                            items.remove(
                                items[position]
                            )
                            notifyDataSetChanged()
                            myProgress.closeDialog()
                            Toast.makeText(
                                context,
                                response.body()!!.message,
                                Toast.LENGTH_LONG
                            )
                                .show();
                        }
                    }

                    override fun onFailure(
                        call: Call<DeletePrescriptionResponse>,
                        t: Throwable?
                    ) {
                        Toast.makeText(
                            context,
                            t?.message,
                            Toast.LENGTH_LONG
                        )
                            .show();
                        myProgress.closeDialog()
                    }
                })
            } catch (ex: Exception) {
                myProgress.closeDialog()
                Toast.makeText(
                    context,
                    ex.message,
                    Toast.LENGTH_LONG
                )
                    .show();
            }

        }

        override fun onBindViewHolder(holder: viewHolder, @SuppressLint("RecyclerView") position: Int) {
            val btnDelete = holder.itemView.findViewById<ImageView>(R.id.card_item_prescription_delete)
            val btnEdit = holder.itemView.findViewById<ImageView>(R.id.card_item_prescription_edit)
            btnDelete.setOnClickListener() {
                deleteItem(holder, position)
            }
            btnEdit.setOnClickListener() {
                editItem(holder, position)
            }
            holder.bind(items[position], context)
        }

        override fun getItemCount() = items.size

        class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val image = itemView.findViewById<ImageView>(R.id.card_item_prescription_image)
            private val title = itemView.findViewById<TextView>(R.id.card_item_prescription_title)
            private val description = itemView.findViewById<TextView>(R.id.card_item_prescription_desc)
            private val date = itemView.findViewById<TextView>(R.id.card_item_prescription_date)
//            private val dosage =
//                itemView.findViewById<TextView>(R.id.card_item_prescription_remind_medicines_text)

            @SuppressLint(
                "SetTextI18n"
            )
            fun bind(
                item: PrescriptionModel,
                context: Context
            ) {

//                image.load("https://blog.ajouve.com/assets/url.jpg") {
//                    crossfade(true)
//                    placeholder(R.drawable.img_login_main)
//                    transformations(CircleCropTransformation())
//                }
//                image.load("https://blog.ajouve.com/assets/url.jpg")
//                Picasso.load("https://blog.ajouve.com/assets/url.jpg").into(image);

//                Glide.with(context)
//                    .load("https://blog.ajouve.com/assets/url.jpg")
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(image)
//                Picasso.with(context.applicationContext)
//                    .load("https://blog.ajouve.com/assets/url.jpg")
//                    .into(image)
                title.text =
                    item.title
                description.text =
                    item.description
                date.text =
                    item.date
//                dosage.text = ""
////                item.medicines?.forEach {
////                    dosage.text = dosage.text.toString() + "\n" + it.toString()
////                }
            }
        }
    }

}