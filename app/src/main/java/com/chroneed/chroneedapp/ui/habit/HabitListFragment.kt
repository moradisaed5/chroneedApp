package com.chroneed.chroneedapp.ui.habit

import android.annotation.SuppressLint
import android.content.Context
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
import com.chroneed.chroneedapp.data.medical.*
import com.chroneed.chroneedapp.databinding.FragmentHabitListBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HabitListFragment : Fragment() {
    private var _binding: FragmentHabitListBinding? = null
    private val binding get() = _binding!!

    private var models: MutableList<MedicalHabitModel> = mutableListOf()
    private lateinit var modelsAdapter: HabitRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHabitListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvents()
        loadData()
    }

    private fun initEvents() {
        val habitAddFragment = HabitAddFragment()
        binding.fragmentHabitListNew.setOnClickListener() {
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_fragment_view_layout, habitAddFragment)
                ?.addToBackStack(null)
                ?.commit()
        }
        binding.fragmentHabitListBack.setOnClickListener() {
            activity?.supportFragmentManager?.popBackStack()

        }
    }
    private fun loadData() {
        val token = "Bearer " + MySharedPreferences.getUserToken(context)//token
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        try {
            var apiInterface = ApiInterface.create(token).getMedicalHabitList(0, 100)
            apiInterface.enqueue(object : Callback<GetMedicalHabitListResponse> {
                override fun onResponse(
                    call: Call<GetMedicalHabitListResponse>,
                    response: Response<GetMedicalHabitListResponse>
                ) {
                    if (response.body()!!.isSuccess == true) {
                        if (response.body()!!.data.count() > 0) {
                            models.clear()//Todo: need to load in first time
                            models.addAll(response.body()!!.data)
                            modelsAdapter = activity?.let { HabitRecyclerAdapter(models, it) }!!

                            binding.fragmentHabitListRecycler.layoutManager = LinearLayoutManager(activity)
                            binding.fragmentHabitListRecycler.adapter = modelsAdapter
                        }
                    }
                    myProgress.closeDialog()
                }

                override fun onFailure(call: Call<GetMedicalHabitListResponse>, t: Throwable?) {
                    Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                    myProgress.closeDialog()
                }
            })
        } catch (ex: Exception) {
            myProgress.closeDialog()
        }
    }



    class HabitRecyclerAdapter constructor(private val items: MutableList<MedicalHabitModel>, val activity: FragmentActivity) :
        RecyclerView.Adapter<HabitRecyclerAdapter.viewHolder>() {
        lateinit var context: Context
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): viewHolder {
            context = parent.context

            val inflater = LayoutInflater.from(parent.context).inflate(R.layout.card_item_habit, parent, false)
            return viewHolder(inflater)
        }

        private fun setGoal(holder: viewHolder,@SuppressLint("RecyclerView") position: Int){
            val id = items[position].id.toString()
            val tokenba = "Bearer " + MySharedPreferences.getUserToken(context)
            var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
            myProgress.showDialog()
            var model= SetGoalRequest(id,1)
            try {
                var apiInterface = ApiInterface.create(tokenba).setGoal(model)
                apiInterface.enqueue(object : Callback<SetGoalResponse> {
                    override fun onResponse(call: Call<SetGoalResponse>, response: Response<SetGoalResponse>) {
                        if (response.body()!!.isSuccess == true) {
                            myProgress.closeDialog()
                            Toast.makeText(context, response.body()!!.message, Toast.LENGTH_LONG).show();
                        }
                    }

                    override fun onFailure(call: Call<SetGoalResponse>, t: Throwable?) {
                        Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                        myProgress.closeDialog()
                    }
                })
            } catch (ex: Exception) {
                myProgress.closeDialog()
                Toast.makeText(context, ex.message, Toast.LENGTH_LONG).show();
            }
        }

        private fun deleteItem(holder: viewHolder, @SuppressLint("RecyclerView") position: Int) {
            val id = items[position].id.toString()
            val tokenba = "Bearer " + MySharedPreferences.getUserToken(context)
            var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
            myProgress.showDialog()

            try {
                var apiInterface = ApiInterface.create(tokenba).deleteMedicalHabit(id = id)
                apiInterface.enqueue(object : Callback<DeleteMedicalHabitResponse> {
                    override fun onResponse(call: Call<DeleteMedicalHabitResponse>, response: Response<DeleteMedicalHabitResponse>) {
                        if (response.body()!!.isSuccess == true) {
                            items.remove(items[position])
                            notifyDataSetChanged()
                            myProgress.closeDialog()
                            Toast.makeText(context, response.body()!!.message, Toast.LENGTH_LONG).show();
                        }
                    }

                    override fun onFailure(call: Call<DeleteMedicalHabitResponse>, t: Throwable?) {
                        Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                        myProgress.closeDialog()
                    }
                })
            } catch (ex: Exception) {
                myProgress.closeDialog()
                Toast.makeText(context, ex.message, Toast.LENGTH_LONG).show();
            }

        }

        private fun editItem(holder: viewHolder, @SuppressLint("RecyclerView") position: Int) {

            val fragment = HabitEditFragment()
            val arguments = Bundle()
            val id = items[position].id
            arguments.putString("id", id)
            fragment.arguments=arguments
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_fragment_view_layout, fragment)
                ?.addToBackStack(null)
                ?.commit()

        }

        override fun onBindViewHolder(holder: viewHolder, position: Int) {
            val btnDelete = holder.itemView.findViewById<ImageView>(R.id.card_item_habits_delete)
            val btnEdit = holder.itemView.findViewById<ImageView>(R.id.card_item_habits_edit)
            val btnSetGoal = holder.itemView.findViewById<ImageView>(R.id.card_item_habits_set_goal)
            btnDelete.setOnClickListener() {
                deleteItem(holder, position)
            }
            btnEdit.setOnClickListener() {
                editItem(holder, position)
            }
            btnSetGoal.setOnClickListener() {
                setGoal(holder, position)
            }
            holder.bind(items[position])
        }

        override fun getItemCount() = items.size
        class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val title = itemView.findViewById<TextView>(R.id.card_item_habit_title)
            private val description = itemView.findViewById<TextView>(R.id.card_item_habit_desc)

            @SuppressLint("SetTextI18n")
            fun bind(item: MedicalHabitModel) {
                title.text = item.title
                description.text = item.description
            }
        }

    }
}