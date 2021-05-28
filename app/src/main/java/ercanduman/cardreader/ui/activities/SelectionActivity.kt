/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ercanduman.cardreader.ui.activities


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ercanduman.cardreader.R
import ercanduman.cardreader.common.IntentData
import ercanduman.cardreader.ui.fragments.SelectionFragment
import org.jmrtd.lds.icao.MRZInfo

class SelectionActivity : AppCompatActivity(), SelectionFragment.SelectionFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        if (null == savedInstanceState) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SelectionFragment(), TAG_SELECTION_FRAGMENT)
                    .commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        var intentData = data
        if (intentData == null) {
            intentData = Intent()
        }
        super.onActivityResult(requestCode, resultCode, intentData)
    }

    override fun onPassportRead(mrzInfo: MRZInfo) {
        val intent = Intent(this, NfcActivity::class.java)
        intent.putExtra(IntentData.KEY_MRZ_INFO, mrzInfo)
        startActivityForResult(intent, REQUEST_NFC)
    }

    companion object {
        private const val REQUEST_NFC = 11

        private const val TAG_SELECTION_FRAGMENT = "TAG_SELECTION_FRAGMENT"
    }
}
