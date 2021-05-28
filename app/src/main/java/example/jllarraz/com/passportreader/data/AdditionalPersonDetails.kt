package example.jllarraz.com.passportreader.data

import android.os.Parcel
import android.os.Parcelable

class AdditionalPersonDetails() : Parcelable {

    var custodyInformation: String? = null
    var fullDateOfBirth: String? = null
    var nameOfHolder: String? = null
    var otherNames: List<String>? = ArrayList()
    var otherValidTDNumbers: List<String>? = ArrayList()
    var permanentAddress: List<String>? = ArrayList()
    var personalNumber: String? = null
    var personalSummary: String? = null
    var placeOfBirth: List<String>? = ArrayList()
    var profession: String? = null
    var proofOfCitizenship: ByteArray? = null
    var tag: Int = 0
    var tagPresenceList: List<Int>? = null
    var telephone: String? = null
    var title: String? = null

    constructor(parcel: Parcel) : this() {
        custodyInformation = parcel.readString()
        fullDateOfBirth = parcel.readString()
        nameOfHolder = parcel.readString()
        otherNames = parcel.createStringArrayList()
        otherValidTDNumbers = parcel.createStringArrayList()
        permanentAddress = parcel.createStringArrayList()
        personalNumber = parcel.readString()
        personalSummary = parcel.readString()
        placeOfBirth = parcel.createStringArrayList()
        profession = parcel.readString()
        proofOfCitizenship = parcel.createByteArray()
        tag = parcel.readInt()
        telephone = parcel.readString()
        title = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(custodyInformation)
        parcel.writeString(fullDateOfBirth)
        parcel.writeString(nameOfHolder)
        parcel.writeStringList(otherNames)
        parcel.writeStringList(otherValidTDNumbers ?: emptyList())
        parcel.writeStringList(permanentAddress)
        parcel.writeString(personalNumber)
        parcel.writeString(personalSummary)
        parcel.writeStringList(placeOfBirth)
        parcel.writeString(profession)
        parcel.writeByteArray(proofOfCitizenship)
        parcel.writeInt(tag)
        parcel.writeString(telephone)
        parcel.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdditionalPersonDetails> {
        override fun createFromParcel(parcel: Parcel): AdditionalPersonDetails {
            return AdditionalPersonDetails(parcel)
        }

        override fun newArray(size: Int): Array<AdditionalPersonDetails?> {
            return arrayOfNulls(size)
        }
    }
}
