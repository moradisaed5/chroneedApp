package com.chroneed.chroneedapp.ui.prescription

import android.Manifest
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.*
import com.chroneed.chroneedapp.data.user.UploadResponseDto
import com.chroneed.chroneedapp.databinding.FragmentPrescriptionEditBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.utilities.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class PrescriptionEditFragment : Fragment() {

    private var _binding: FragmentPrescriptionEditBinding? = null
    private val binding get() = _binding!!
    private lateinit var id: String
    //selected medicines
    var selectedDrugs = arrayListOf<String>()
    private var requestCode = 0

    //added medicine
    var addedDrugs: MutableList<PrescriptionMedicineModel> = mutableListOf()

    //temp image path before upload it
    private var mCurrentPhotoPath: String? = null

    //image file name on server after upload
    private var uploadedFileName: String? = null


    //---------------------Prescription
    private lateinit var btnBack: MaterialButton
    private lateinit var btnSave: MaterialButton
    private lateinit var txtTitle: TextInputEditText
    private lateinit var txtDescription: TextInputEditText
    private lateinit var txtPrescriptionDate: TextInputEditText
    private lateinit var btnNewMedicine: MaterialButton
    private lateinit var txtDrugList: MaterialAutoCompleteTextView
    private lateinit var medicineListRecycler: RecyclerView
    private lateinit var btnNewMedicineFromGallery: ImageView

    //--------------------AddMedicine
    private lateinit var btnAddMedicine: MaterialButton
    private lateinit var btnBackAddMedicine: MaterialButton
    private lateinit var txtMedicineName: TextInputEditText
    private lateinit var txtMedicineNote: TextInputEditText
    private lateinit var txtMedicineType: MaterialAutoCompleteTextView
    private lateinit var btnMedicineGallery: ImageView
    private lateinit var btnMedicineImage: ImageView
    private lateinit var btnMedicineImageClear: Button
    private lateinit var txtMedicineAdministration: TextInputEditText
    private lateinit var txtMedicineQty: TextInputEditText
    private lateinit var txtMedicineDosageEvery: Spinner
    private lateinit var txtMedicineStartUsageDate: TextInputEditText
    private lateinit var txtMedicineStartUsageTime: TextInputEditText
    private lateinit var txtMedicineUsageTimeDuration: TextInputEditText
    private lateinit var sectionWeekDays: TextInputLayout
    private lateinit var sectionDuration: TextInputLayout
    private lateinit var sectionOccurrence: TextInputLayout
    private lateinit var sectionEndDate: TextInputLayout
    private lateinit var btnWeekDaySU: ToggleButton
    private lateinit var btnWeekDayMo: ToggleButton
    private lateinit var btnWeekDayTu: ToggleButton
    private lateinit var btnWeekDayWe: ToggleButton
    private lateinit var btnWeekDayTh: ToggleButton
    private lateinit var btnWeekDayFr: ToggleButton
    private lateinit var btnWeekDaySa: ToggleButton
    private lateinit var btnUseTimeTypeNever: RadioButton
    private lateinit var btnUseTimeTypeOn: RadioButton
    private lateinit var btnUseTimeTypeAfter: RadioButton
    private lateinit var txtMedicineOccurrence: TextInputEditText
    private lateinit var txtMedicineEndDate: TextInputEditText
    //---------------------objects------------------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            id = bundle.getString("id", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPrescriptionEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObjects()
        initElements()
        initEvents()
        showAddPrescription()
        loadData()
    }


    private fun initObjects() {
        //-------------------init Prescription------------
        btnBack = binding.fragmentPrescriptionEditBack
        btnSave = binding.fragmentPrescriptionEditBtnSave
        txtTitle = binding.fragmentPrescriptionEditTitle
        txtDescription = binding.fragmentPrescriptionEditDescription
        txtPrescriptionDate = binding.fragmentPrescriptionEditFormDateInput
        btnNewMedicine = binding.fragmentPrescriptionEditAddDrug
        txtDrugList = binding.fragmentPrescriptionEditFormDrugList
        medicineListRecycler = binding.fragmentPrescriptionEditMedicineList
        btnNewMedicineFromGallery = binding.fragmentPrescriptionEditNewMedicineFromGallery
        //-------------------init add Prescription------------
        btnBackAddMedicine = binding.fragmentPrescriptionEditNewMedicineBtnBack
        btnAddMedicine = binding.fragmentPrescriptionEditNewMedicineBtnAddMedicine
        txtMedicineName = binding.fragmentPrescriptionEditNewMedicineName
        txtMedicineNote = binding.fragmentPrescriptionEditNewMedicineNote
        txtMedicineType = binding.fragmentPrescriptionEditNewMedicineMedicineType
        btnMedicineGallery = binding.fragmentPrescriptionEditNewMedicineGallery
        btnMedicineImage = binding.fragmentPrescriptionEditNewMedicineImageShow
        btnMedicineImageClear = binding.fragmentPrescriptionEditNewMedicineBtnClearImage
        txtMedicineAdministration = binding.fragmentPrescriptionEditNewMedicineAdministration
        txtMedicineQty = binding.fragmentPrescriptionEditNewMedicineQty
        txtMedicineDosageEvery = binding.fragmentPrescriptionEditNewMedicineDosageEvery
        txtMedicineStartUsageDate = binding.fragmentPrescriptionEditNewMedicineStartUsageDate
        txtMedicineStartUsageTime = binding.fragmentPrescriptionEditNewMedicineUsageTime
        txtMedicineUsageTimeDuration = binding.fragmentPrescriptionEditNewMedicineUsageTimeDurationTime
        sectionWeekDays = binding.fragmentPrescriptionEditNewMedicineDosageEveryWeekDays
        sectionDuration = binding.fragmentPrescriptionEditNewMedicineDosageEveryDurationSection
        sectionOccurrence = binding.fragmentPrescriptionEditNewMedicineOccurrenceSection
        sectionEndDate = binding.fragmentPrescriptionEditNewMedicineEndDateSection
        btnWeekDaySU = binding.fragmentPrescriptionEditNewMedicineWeekDaySU
        btnWeekDayMo = binding.fragmentPrescriptionEditNewMedicineWeekDayMo
        btnWeekDayTu = binding.fragmentPrescriptionEditNewMedicineWeekDayTu
        btnWeekDayWe = binding.fragmentPrescriptionEditNewMedicineWeekDayWe
        btnWeekDayTh = binding.fragmentPrescriptionEditNewMedicineWeekDayTh
        btnWeekDayFr = binding.fragmentPrescriptionEditNewMedicineWeekDayFr
        btnWeekDaySa = binding.fragmentPrescriptionEditNewMedicineWeekDaySa
        btnUseTimeTypeNever = binding.fragmentPrescriptionEditNewMedicineUseTimeTypeNever
        btnUseTimeTypeOn = binding.fragmentPrescriptionEditNewMedicineUseTimeTypeOn
        btnUseTimeTypeAfter = binding.fragmentPrescriptionEditNewMedicineUseTimeTypeAfter
        txtMedicineOccurrence = binding.fragmentPrescriptionEditNewMedicineOccurrence
        txtMedicineEndDate = binding.fragmentPrescriptionEditNewMedicineEndDate
    }

    private fun fillForm(model: PrescriptionModel) {
        //fill prescription
        txtTitle.setText(model.title)
        txtDescription.setText(model.description)
        txtPrescriptionDate.setText(model.date)
        model.medicines?.let { addedDrugs.addAll(it.toMutableList()) }
        reloadAdaptor()
    }

    private fun loadData() {
        val token = "Bearer " + MySharedPreferences.getUserToken(context)
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        try {
            var apiInterface = ApiInterface.create(token).getPrescription(id = id)
            apiInterface.enqueue(object : Callback<GetPrescriptionModelResponse> {
                override fun onResponse(call: Call<GetPrescriptionModelResponse>, response: Response<GetPrescriptionModelResponse>) {
                    if (response.body()!!.isSuccess == true) {
                        myProgress.closeDialog()
//                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_LONG).show()
                        response.body()!!.data?.let { fillForm(it) }

                    }
                }

                override fun onFailure(call: Call<GetPrescriptionModelResponse>, t: Throwable?) {
                    Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                    myProgress.closeDialog()
                }
            })
        } catch (ex: Exception) {
            myProgress.closeDialog()
            Toast.makeText(context, ex.message, Toast.LENGTH_LONG).show();
        }
    }






    private fun showAddPrescription() {
        binding.fragmentPrescriptionEditNewMedicine.visibility = View.GONE
        binding.fragmentPrescriptionEdit.visibility = View.VISIBLE
        btnSave.visibility = View.VISIBLE
    }

    private fun showAddPrescriptionMedicine() {
       binding.fragmentPrescriptionEdit.visibility = View.GONE
        binding.fragmentPrescriptionEditNewMedicine.visibility = View.VISIBLE
    }

    private fun hideKeyboard() {
        Handler().postDelayed({
            val inputMethodManager =
                this.activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }, 1)
    }

    //initial events
    private fun initEvents() {
        //-----------------------------------------prescription section-----------------------------------------------------
        //medicine list click
        medicinesListClick()

        //add medicine click
        btnNewMedicine.setOnClickListener() {
            showAddPrescriptionMedicine()
        }

        //back from add prescription
        btnBack.setOnClickListener() {
            activity?.supportFragmentManager?.popBackStack()
        }

        //prescription date focused
        txtPrescriptionDate.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                selectPrescriptionDate()
                hideKeyboard()
            }


        }

        //prescription date click
        txtPrescriptionDate.setOnClickListener() {
            selectPrescriptionDate()
            hideKeyboard()
        }

        //medicine text change
        txtDrugList.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (txtDrugList.text.length == 3) {
                    searchMedicine(txtDrugList.text.toString())
                }
            }
        })

        //on button save click
        btnSave.setOnClickListener() {
            savePrescription()
        }

        //-----------------------------------------prescription section-----------------------------------------------------

        //-------------------------------------------medicine section-------------------------------------------------------
        //back from add medicine
        btnBackAddMedicine.setOnClickListener() {
            showAddPrescription()
        }

        //medicine usage date focused
        txtMedicineEndDate.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                selectMedicineUsageDate()
                hideKeyboard()
            }

        }

        //medicine usage date click
        txtMedicineEndDate.setOnClickListener() {
            selectMedicineUsageDate()
            hideKeyboard()
        }

        //medicine start usage date focused
        txtMedicineStartUsageDate.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                selectMedicineStartUsageDate()
                hideKeyboard()
            }
        }
        //medicine start usage date click
        txtMedicineEndDate.setOnClickListener() {
            selectMedicineStartUsageDate()
            hideKeyboard()
        }



        //gallery click
        btnMedicineGallery.setOnClickListener() {
            pickImageFromGallery()
        }

        //prescription from gallery
        btnNewMedicineFromGallery.setOnClickListener() {
            pickPrescriptionImageFromGallery()
        }

        //btn clear image click
        btnMedicineImageClear.setOnClickListener() {
            mCurrentPhotoPath = ""
            showMedicineSelectImage()

        }

        //use time type click
        btnUseTimeTypeNever.setOnClickListener() {
            selectMedicineEndTime()
        }

        //use time type click
        btnUseTimeTypeOn.setOnClickListener() {
            selectMedicineEndTime()
        }

        //use time type click
        btnUseTimeTypeAfter.setOnClickListener() {
            selectMedicineEndTime()
        }

        //select use medicine very
        medicineUseEvery()

        //select use time
        selectUsageTime()

        //add medicine record
        btnAddMedicine.setOnClickListener() {
            addMedicineRecord()
        }

        txtMedicineUsageTimeDuration.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                showWeekItems()
            }
        })
        //-------------------------------------------medicine section-------------------------------------------------------

    }

    // save prescription
    private fun savePrescription() {
        val token = "Bearer " + MySharedPreferences.getUserToken(context)
        val updatemodel = UpdatePrescriptionModel(
            id=id,
            title = txtTitle.text.toString(),
            description = txtDescription.text.toString(),
            date = txtPrescriptionDate.text.toString(),
            medicines = addedDrugs
        )
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        var apiInterface = ApiInterface.create(token).updatePrescription(updatemodel)
        myProgress.showDialog()
        apiInterface.enqueue(object : Callback<GetPrescriptionModelResponse> {
            override fun onResponse(
                call: Call<GetPrescriptionModelResponse>,
                response: Response<GetPrescriptionModelResponse>
            ) {
                myProgress.closeDialog()
                if (response.code() == 200) {
                    if (response.body()!!.isSuccess == true) {
                        initAddPrescriptionElement()
                        initAddPrescriptionMedicineElement()
                        activity?.supportFragmentManager?.popBackStack()
                    } else {
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_LONG)
                            .show();
                    }
                } else {
                    Toast.makeText(context, response.body()!!.message, Toast.LENGTH_LONG)
                        .show();
                }
            }

            override fun onFailure(call: Call<GetPrescriptionModelResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }

    //medicine list click
    private fun medicinesListClick() {
//        medicineListRecycler.addOnItemTouchListener() =
//            OnItemClickListener { parent, itemView, position, id ->
//                val alertDialog: AlertDialog? = activity?.let {
//                    val builder = AlertDialog.Builder(it)
//                    builder.apply {
//                        setTitle("do you want to delete item ?")
//                        setPositiveButton("Yes",
//                            DialogInterface.OnClickListener { dialog, dialogId ->
//                                selectedDrugs.remove(selectedDrugs[position])
//                                val arrayAdapter: ArrayAdapter<*>
//                                arrayAdapter = ArrayAdapter(
//                                    thisContext,
//                                    android.R.layout.simple_list_item_1, selectedDrugs
//                                )
//                                medicineListRecycler.adapter = arrayAdapter
//                            })
//                        setNegativeButton("Cancel",
//                            DialogInterface.OnClickListener { dialog, dialogId ->
//                            })
//                    }
//                    builder.create()
//                }
//                alertDialog?.show()
//            }
    }

    private fun medicineUseEvery() {
        txtMedicineDosageEvery.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    showWeekItems()
                }
            }
    }

    //click on prescription date and select
    private fun selectPrescriptionDate() {
        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)
            txtPrescriptionDate.setText(dateFormat.format(myCalendar.time))


        }

        this.context?.let { it ->
            DatePickerDialog(
                it, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
                    Calendar.MONTH
                ),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    //select medicine usage time
    private fun selectUsageTime() {

        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            val myFormat = "HH:mm"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)
            txtMedicineStartUsageTime.setText(dateFormat.format(cal.time))
        }

        txtMedicineStartUsageTime.setOnClickListener {
            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
            hideKeyboard()
        }
        txtMedicineStartUsageTime.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                TimePickerDialog(
                    context,
                    timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true
                ).show()
                hideKeyboard()
            }
        }

    }

    //select medicine usage date
    private fun selectMedicineUsageDate() {
        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)
            txtMedicineEndDate.setText(dateFormat.format(myCalendar.time))
        }

        this.context?.let { it ->
            DatePickerDialog(
                it, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
                    Calendar.MONTH
                ),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun selectMedicineStartUsageDate() {
        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)
            txtMedicineStartUsageDate.setText(
                dateFormat.format(
                    myCalendar.time
                )
            )
        }

        this.context?.let { it ->
            DatePickerDialog(
                it, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
                    Calendar.MONTH
                ),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    //show week items where select dosage every week
    private fun showWeekItems() {

        val selectedItem =
            txtMedicineDosageEvery.selectedItem.toString()


        if (selectedItem.toLowerCase() == "week") {
            sectionWeekDays.visibility = View.VISIBLE
            sectionDuration.visibility =
                View.GONE
        } else {
            sectionWeekDays.visibility = View.GONE
            sectionDuration.visibility =
                View.VISIBLE
            var duration = 0
            try {
                duration =
                    txtMedicineUsageTimeDuration.text.toString()
                        .toInt()
            } catch (ex: Exception) {
            }
            val showText = "Use every $duration $selectedItem"
            sectionDuration.hint = showText
//            Toast.makeText(context, showText, Toast.LENGTH_SHORT).show()

        }
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(binding.root.context) // optional usage

            val imageFile = File(uriFilePath)
            if (imageFile.isFile) {
                mCurrentPhotoPath = uriFilePath

                if (requestCode == CodeManager.RETURN_IMAGE_PRESCRIPTION_MEDICINE) {
                    binding.fragmentPrescriptionEditNewMedicineImageShow.setImageURI(null)
                    binding.fragmentPrescriptionEditNewMedicineImageShow.refreshDrawableState()
                    binding.fragmentPrescriptionEditNewMedicineImageShow.setImageURI(Uri.fromFile(imageFile))
                    binding.fragmentPrescriptionEditNewMedicineImageShow.refreshDrawableState()
                    showMedicineImage()
                } else if (requestCode == CodeManager.RETURN_IMAGE_PRESCRIPTION_OCR) {
//                    binding.fragmentPrescriptionAddNewMedicineImageShow.setImageURI(null)
//                    binding.fragmentPrescriptionAddNewMedicineImageShow.refreshDrawableState()
//                    binding.fragmentPrescriptionAddNewMedicineImageShow.setImageURI(Uri.fromFile(imageFile))
//                    binding.fragmentPrescriptionAddNewMedicineImageShow.refreshDrawableState()
                    Toast.makeText(this.context, "image selected", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    //pick up image from gallery
    private fun pickImageFromGallery() {
        requestCode = CodeManager.RETURN_IMAGE_PRESCRIPTION_MEDICINE
        cropImage.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
                setActivityTitle("select your medicine image")
            }
        )
    }

    private fun pickPrescriptionImageFromGallery() {
        requestCode = CodeManager.RETURN_IMAGE_PRESCRIPTION_OCR
        cropImage.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
                setActivityTitle("select prescription image")
            }
        )
    }


    //initial elements
    private fun initElements() {
        try {
            selectedDrugs.clear()
        } catch (message: Exception) {
        }
        initAddPrescriptionElement() //Todo : need to change
        initAddPrescriptionMedicineElement() //Todo : need to change
    }

    //initial element
    private fun initAddPrescriptionElement() {
        btnSave.visibility = View.VISIBLE
        txtTitle.setText("")
        txtDescription.setText("")
        txtPrescriptionDate.setText("")
    }

    //add medicine item
    private fun initAddPrescriptionMedicineElement() {
        txtMedicineName.setText("")//init title
        txtMedicineNote.setText("")//init note
        txtMedicineType.setText("")//init medicine type
        showMedicineSelectImage()
        txtMedicineAdministration.setText("")//init administration
        txtMedicineQty.setText("")//init dosage
        txtMedicineDosageEvery.setSelection(0)//init dosage every
        txtMedicineStartUsageTime.setText("")//init usage time
        txtMedicineUsageTimeDuration.setText("0")//init usage time
        btnUseTimeTypeNever.isChecked = true

        selectMedicineEndTime()
    }

    //show camera and select gallery in image section and hide image and clear button
    private fun showMedicineSelectImage() {
        //show elements
        btnMedicineGallery.visibility = View.VISIBLE
        //hide image
        btnMedicineImage.visibility = View.GONE
        btnMedicineImageClear.visibility = View.GONE
    }

    // select end time
    private fun selectMedicineEndTime() {
        if (btnUseTimeTypeNever.isChecked) {//select end time never in new medicine
            sectionEndDate.visibility = View.GONE
            sectionOccurrence.visibility = View.GONE
        } else if (btnUseTimeTypeOn.isChecked) { //select end time on in new medicine
            sectionEndDate.visibility = View.VISIBLE
            sectionOccurrence.visibility = View.GONE
        } else if (btnUseTimeTypeAfter.isChecked) { //select end time after in new medicine
            sectionEndDate.visibility = View.GONE
            sectionOccurrence.visibility = View.VISIBLE
        } else {
            btnUseTimeTypeNever.isChecked = true
            sectionEndDate.visibility = View.GONE
            sectionOccurrence.visibility = View.GONE
        }
    }


    //create file for image capture
    private fun createFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        val storageDir: File? = this.context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }


    //bind medicine list Todo : need to change
    private fun bindMedicineList(model: List<String>) {
        val drugAdapter = this.context?.let {
            ArrayAdapter<String>(it, android.R.layout.simple_selectable_list_item, model)
        }
        try {
            txtDrugList.setAdapter(drugAdapter)
        } catch (message: Exception) {
        }
    }

    //search medicine Todo : need to change
    private fun searchMedicine(SearchText: String) {
        //call api
        var apiInterface = ApiInterface.create("").searchMedicineListV2(SearchText)
        apiInterface.enqueue(object : Callback<SearchMedicineListModelV2Response> {
            override fun onResponse(
                call: Call<SearchMedicineListModelV2Response>,
                response: Response<SearchMedicineListModelV2Response>
            ) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data.count() > 0) {
                        bindMedicineList(response.body()!!.data)
                    }
                }
            }

            override fun onFailure(call: Call<SearchMedicineListModelV2Response>, t: Throwable?) {
                Toast.makeText(binding.root.context, t?.message, Toast.LENGTH_LONG).show();
            }
        })
    }


    //hide camera and select gallery in image section and show image and clear button
    private fun showMedicineImage() {
        //hide camera and gallery icons
        btnMedicineGallery.visibility = View.GONE
        //show image
        btnMedicineImage.visibility = View.VISIBLE
        btnMedicineImageClear.visibility = View.VISIBLE
    }


    //upload file Todo : need to change
    private fun addMedicineItem(model: PrescriptionMedicineModel) {
        var loadingProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        loadingProgress.showDialog()

        if (mCurrentPhotoPath == null)
            mCurrentPhotoPath = ""

        if (mCurrentPhotoPath!!.isEmpty()) {
            mCurrentPhotoPath = ""
            uploadedFileName = ""
            model.imageName = ""
            addedDrugs.add(model)
            showAddPrescription()
            initAddPrescriptionMedicineElement()
            reloadAdaptor()
            loadingProgress.closeDialog()
            return
        }

        var token = "Bearer " + MySharedPreferences.getUserToken(context)
        var profilePic: MultipartBody.Part? = null
        if (mCurrentPhotoPath != null) {
            uploadedFileName = ""
            val file = File(mCurrentPhotoPath)
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            profilePic = MultipartBody.Part.createFormData("file", file.name, requestFile)
            var apiInterface = ApiInterface.create(token).uploadFile(profilePic)
            apiInterface.enqueue(object : Callback<UploadResponseDto> {
                override fun onResponse(
                    call: Call<UploadResponseDto>,
                    response: Response<UploadResponseDto>
                ) {
                    if (response.code() == 200) {
                        if (response.body()!!.isSuccess) {
                            if (response.body()!!.data != null) {
                                if (response.body()!!.data?.count() == 1) {
                                    try {
                                        uploadedFileName = response.body()!!.data?.first()?.fileName
                                    } catch (ex: Exception) {
                                    }
                                }
                            }
                        } else
                            Toast.makeText(context, "problem to upload file!", Toast.LENGTH_LONG)
                                .show();
                    } else
                        Toast.makeText(context, "problem to upload file!", Toast.LENGTH_LONG)
                            .show();

                    if (uploadedFileName!!.isNotEmpty()) {
                        mCurrentPhotoPath = ""
                        model.imageName = uploadedFileName
                        uploadedFileName = ""
                        addedDrugs.add(model)
                        showAddPrescription()
                        reloadAdaptor()
                        initAddPrescriptionMedicineElement()
                    } else {
                        Toast.makeText(context, "problem to upload file!", Toast.LENGTH_LONG)
                            .show()
                        return
                    }
                    loadingProgress.closeDialog()
                }

                override fun onFailure(call: Call<UploadResponseDto>, t: Throwable?) {
                    loadingProgress.closeDialog()
                    Toast.makeText(context, "can't connect to server!", Toast.LENGTH_LONG).show();
                    return
                }
            })

        }
    }

    //add medicine record Todo : need to change
    fun addMedicineRecord() {
        //get model
        var model = getMedicineRecordModel() ?: return
        val validateResult = validateModel(model)
        if (!validateResult) return
        addMedicineItem(model)
    }

    private fun reloadAdaptor() {
        Handler().postDelayed({
            var modelsAdapter = PrescriptionMedicinesRecyclerAdapter(addedDrugs)
            medicineListRecycler.layoutManager = LinearLayoutManager(this.activity)
            medicineListRecycler.adapter = modelsAdapter
        }, 200)
    }

    private fun validateModel(model: PrescriptionMedicineModel): Boolean {
        if (model == null) {
            Toast.makeText(this.context, "some error i get data", Toast.LENGTH_LONG).show()
            return false
        }
        if (model.name == null || model.name == "") {
            Toast.makeText(this.context, "enter medicine name", Toast.LENGTH_SHORT).show()
            txtMedicineName.requestFocus()
            return false
        }
        if (model.administration == null || model.administration == "") {
            Toast.makeText(this.context, "enter medicine name", Toast.LENGTH_SHORT).show()
            txtMedicineAdministration.requestFocus()
            return false
        }
        return true
    }

    //get medicine model form form Todo : need to change
    private fun getMedicineRecordModel(): PrescriptionMedicineModel {
        var daysOfWeek: MutableList<Int> = mutableListOf()
        var name = "";
        var note = "";
        var medicineType = "";
        var administration = "";
        var qty = "";
        var startUsingDate = "";
        var startUsingTime = "";
        var dosageEvery = "";
        var useEveryDuration = 0
        var endTimeName = ""
        var endDate = ""
        var endOccurrence = 0
        val imageName = mCurrentPhotoPath
        try {
            name = txtMedicineName.text.toString()
        } catch (ex: Exception) {
        }
        try {
            note = txtMedicineNote.text.toString()
        } catch (ex: Exception) {
        }
        try {
            medicineType = txtMedicineType.text.toString()
        } catch (ex: Exception) {
        }
        try {
            administration = txtMedicineAdministration.text.toString()
        } catch (ex: Exception) {
        }
        try {
            qty = txtMedicineQty.text.toString()
        } catch (ex: Exception) {
        }
        try {
            startUsingDate = txtMedicineStartUsageDate.text.toString()
        } catch (ex: Exception) {
        }
        try {
            startUsingTime = txtMedicineStartUsageTime.text.toString()
        } catch (ex: Exception) {
        }
        try {
            dosageEvery = txtMedicineDosageEvery.selectedItem.toString()
        } catch (ex: Exception) {
        }

        //get week days
        if (dosageEvery == "week") {
            if (btnWeekDaySU.isChecked)
                daysOfWeek.add(enumDayOfWeek.Su.value)
            if (btnWeekDayMo.isChecked)
                daysOfWeek.add(enumDayOfWeek.Mo.value)
            if (btnWeekDayTu.isChecked)
                daysOfWeek.add(enumDayOfWeek.Tu.value)
            if (btnWeekDayWe.isChecked)
                daysOfWeek.add(enumDayOfWeek.We.value)
            if (btnWeekDayTh.isChecked)
                daysOfWeek.add(enumDayOfWeek.Th.value)
            if (btnWeekDayFr.isChecked)
                daysOfWeek.add(enumDayOfWeek.Fr.value)
            if (btnWeekDaySa.isChecked)
                daysOfWeek.add(enumDayOfWeek.Sa.value)
        } else {
            try {
                useEveryDuration = txtMedicineUsageTimeDuration.toString().toInt()
            } catch (ex: Exception) {
            }
        }
        //----------------------- end time name -----------------
        if (btnUseTimeTypeNever.isChecked)
            endTimeName = "never"
        if (btnUseTimeTypeOn.isChecked) {
            endTimeName = "on"
            try {
                endDate = txtMedicineEndDate.text.toString()
            } catch (ex: Exception) {
            }

        }
        if (btnUseTimeTypeAfter.isChecked) {
            endTimeName = "after"
            try {
                endOccurrence =
                    txtMedicineOccurrence.text.toString().toInt()
            } catch (ex: Exception) {
            }

        }

        val model = PrescriptionMedicineModel(
            dayOfWeek = daysOfWeek,
            note = note,
            administration = administration,
            qty = qty,
            dosageEvery = dosageEvery,
            startUsingDate = startUsingDate,
            startUsingTime = startUsingTime,
            endUsingTimeName = endTimeName,
            endUsingTimeValue = endDate,
            imageName = imageName,
            medicineType = medicineType,
            name = name,
            endOccurrence = endOccurrence,
            useEveryDuration = useEveryDuration
        )
        return model
    }

    //set auto alarm for test Todo : this is must to change
    private fun setAutoAlarm(sec: Int) {
        val calendar = Calendar.getInstance()
        val alarmManager = this.activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this.activity, AlarmReceiver::class.java)
        intent.putExtra("alarm_message", "this message send from prescription"); //data to pass
        intent.putExtra("alarm_title", "this is my title"); //data to pass
        intent.putExtra("alarm_service_name", "this is service name"); //data to pass
        val pendingIntent = PendingIntent.getBroadcast(
            this.activity, CodeManager.REQUEST_ALARM_CODE,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis + (1 * sec * 1000),
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    class PrescriptionMedicinesRecyclerAdapter constructor(private val items: MutableList<PrescriptionMedicineModel>) :
        RecyclerView.Adapter<PrescriptionMedicinesRecyclerAdapter.viewHolder>() {
        private lateinit var context: Context
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): viewHolder {

            context = parent.context
            val inflater = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_item_prescription_added_medicine, parent, false)
            return viewHolder(inflater)
        }

        override fun onBindViewHolder(holder: viewHolder, position: Int) {
            val imageName = items[position].imageName
            val imageView =holder.itemView.findViewById<ImageView>(R.id.card_item_prescription_added_medicine_image)
            val btnDelete =holder.itemView.findViewById<ImageView>(R.id.card_item_prescription_added_medicine_delete)
            val btnEdit =holder.itemView.findViewById<ImageView>(R.id.card_item_prescription_added_medicine_edit)
            if (imageName != null) {
                if (imageName!!.isNotEmpty()) {
                    Glide.with(context).load("http://chroneed.saeid-moradi.ir/files/$imageName")
                        .into(imageView)
                }
            }
            btnDelete.setOnClickListener() {
                Toast.makeText(this.context, "delete " + items[position].name, Toast.LENGTH_SHORT)
                    .show()
                items.remove(items[position])
                notifyDataSetChanged()

            }
            btnEdit.setOnClickListener() {
                Toast.makeText(this.context, "edit " + items[position].name, Toast.LENGTH_SHORT)
                    .show()

            }
            holder.bind(items[position])
        }

        override fun getItemCount() = items.size
        class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val title =
                itemView.findViewById<TextView>(R.id.card_item_prescription_added_medicine_title)
            private val medicineType =
                itemView.findViewById<TextView>(R.id.card_item_prescription_added_medicine_medicineType)
            private val adminstration =
                itemView.findViewById<TextView>(R.id.card_item_prescription_added_medicine_administration)

            @SuppressLint("SetTextI18n")
            fun bind(item: PrescriptionMedicineModel) {
                title.text = item.name
                adminstration.text = item.administration
                medicineType.text = item.medicineType
            }
        }
    }

}