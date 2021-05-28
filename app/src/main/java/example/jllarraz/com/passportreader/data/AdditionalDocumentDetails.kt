package example.jllarraz.com.passportreader.data

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import java.util.*

class AdditionalDocumentDetails() : Parcelable {

    var endorsementsAndObservations: String? = null
    var dateAndTimeOfPersonalization: String? = null
    var dateOfIssue: String? = null
    var imageOfFront: Bitmap? = null
    var imageOfRear: Bitmap? = null
    var issuingAuthority: String? = null
    var namesOfOtherPersons: List<String>? = ArrayList()
    var personalizationSystemSerialNumber: String? = null
    var taxOrExitRequirements: String? = null
    var tag: Int = 0
    var tagPresenceList: List<Int> = ArrayList()

    constructor(parcel: Parcel) : this() {
        endorsementsAndObservations = parcel.readString()
        dateAndTimeOfPersonalization = parcel.readString()
        dateOfIssue = parcel.readString()
        imageOfFront = parcel.readParcelable(Bitmap::class.java.classLoader)
        imageOfRear = parcel.readParcelable(Bitmap::class.java.classLoader)
        issuingAuthority = parcel.readString()
        namesOfOtherPersons = parcel.createStringArrayList()
        personalizationSystemSerialNumber = parcel.readString()
        taxOrExitRequirements = parcel.readString()
        tag = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(endorsementsAndObservations)
        parcel.writeString(dateAndTimeOfPersonalization)
        parcel.writeString(dateOfIssue)
        parcel.writeParcelable(imageOfFront, flags)
        parcel.writeParcelable(imageOfRear, flags)
        parcel.writeString(issuingAuthority)
        parcel.writeStringList(namesOfOtherPersons)
        parcel.writeString(personalizationSystemSerialNumber)
        parcel.writeString(taxOrExitRequirements)
        parcel.writeInt(tag)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdditionalDocumentDetails> {
        override fun createFromParcel(parcel: Parcel): AdditionalDocumentDetails {
            return AdditionalDocumentDetails(parcel)
        }

        override fun newArray(size: Int): Array<AdditionalDocumentDetails?> {
            return arrayOfNulls(size)
        }
    }
}
