/*
 * Copyright 2020 RethinkDNS and its authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.celzero.bravedns.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.toLiveData
import com.celzero.bravedns.database.BlockedConnectionsDAO
import com.celzero.bravedns.util.Constants.Companion.FILTER_IS_FILTER
import com.celzero.bravedns.util.Constants.Companion.LIVEDATA_PAGE_SIZE

class BlockedConnectionsViewModel(private val blockedConnectionsDAO: BlockedConnectionsDAO) :
        ViewModel() {

    private var filteredList: MutableLiveData<String> = MutableLiveData()

    init {
        filteredList.value = ""
    }

    var blockedUnivRulesList = Transformations.switchMap(filteredList) { input ->
        if (input.isNullOrBlank()) {
            blockedConnectionsDAO.getUnivBlockedConnectionsLiveData().toLiveData(
                pageSize = LIVEDATA_PAGE_SIZE)
        } else if (FILTER_IS_FILTER == input) {
            blockedConnectionsDAO.getUnivBlockedConnectionsLiveData().toLiveData(
                pageSize = LIVEDATA_PAGE_SIZE)
        } else {
            blockedConnectionsDAO.getUnivBlockedConnectionsByIP("%$input%").toLiveData(
                LIVEDATA_PAGE_SIZE)
        }
    }

    fun setFilter(filter: String) {
        filteredList.value = filter
    }

    fun setFilterBlocked(filter: String) {
        filteredList.value = filter
    }
}
