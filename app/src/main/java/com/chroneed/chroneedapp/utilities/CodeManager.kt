package com.chroneed.chroneedapp.utilities

class CodeManager {
    companion object {
        //request for alarm manager to remind something
        const val REQUEST_ALARM_CODE = 1

        //request for camera permission access and return in activity result
        const val REQUEST_CAMERA_PERMISSION_CODE = 2

        //request for image capture
        const val REQUEST_IMAGE_CAPTURE = 3



        //return image pick up code
        const val RETURN_IMAGE_PICK_UP_CODE = 10

        //return prescription image pick up
        const val RETURN_Prescription_IMAGE_PICK_UP_CODE = 11

        //return prescription image gallery
        const val RETURN_Prescription_IMAGE_Camera_CODE = 12

        //return camera crop image
        const val RETURN_IMAGE_CAMERA_CROP_CODE = 13

        //request image for prescription ocr
        const val RETURN_IMAGE_PRESCRIPTION_OCR = 14

        //request image for prescription medicine
        const val RETURN_IMAGE_PRESCRIPTION_MEDICINE = 15

        //request image for prescription medicine
        const val RETURN_IMAGE_Alarm_MEDICINE = 16

        const val REQUEST_SPEECH_GLOBAL_CODE = 21
        const val REQUEST_SPEECH_TRAIN_CODE = 22

    }
}