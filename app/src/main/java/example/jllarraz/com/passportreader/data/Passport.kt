package example.jllarraz.com.passportreader.data

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import org.jmrtd.FeatureStatus
import org.jmrtd.VerificationStatus
import org.jmrtd.lds.SODFile

class Passport() : Parcelable {
    var sodFile: SODFile? = null
    var face: Bitmap? = null
    var portrait: Bitmap? = null
    var signature: Bitmap? = null
    var fingerprints: List<Bitmap>? = null
    var personDetails: PersonDetails? = null
    var additionalPersonDetails: AdditionalPersonDetails? = null
    var additionalDocumentDetails: AdditionalDocumentDetails? = null
    var featureStatus: FeatureStatus? = null
    var verificationStatus: VerificationStatus? = null

    constructor(parcel: Parcel) : this() {
        face = parcel.readParcelable(Bitmap::class.java.classLoader)
        portrait = parcel.readParcelable(Bitmap::class.java.classLoader)
        signature = parcel.readParcelable(Bitmap::class.java.classLoader)
        fingerprints = parcel.createTypedArrayList(Bitmap.CREATOR)
        personDetails = parcel.readParcelable(PersonDetails::class.java.classLoader)
        additionalPersonDetails = parcel.readParcelable(AdditionalPersonDetails::class.java.classLoader)
        additionalDocumentDetails = parcel.readParcelable(AdditionalDocumentDetails::class.java.classLoader)
        featureStatus = parcel.readParcelable(FeatureStatus::class.java.classLoader)
        verificationStatus = parcel.readParcelable(VerificationStatus::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(face, flags)
        parcel.writeParcelable(portrait, flags)
        parcel.writeParcelable(signature, flags)
        parcel.writeTypedList(fingerprints)
        parcel.writeParcelable(personDetails, flags)
        parcel.writeParcelable(additionalPersonDetails, flags)
        parcel.writeParcelable(additionalDocumentDetails, flags)
        parcel.writeParcelable(featureStatus, flags)
        parcel.writeParcelable(verificationStatus, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Passport> {
        override fun createFromParcel(parcel: Parcel): Passport {
            return Passport(parcel)
        }

        override fun newArray(size: Int): Array<Passport?> {
            return arrayOfNulls(size)
        }
    }
}