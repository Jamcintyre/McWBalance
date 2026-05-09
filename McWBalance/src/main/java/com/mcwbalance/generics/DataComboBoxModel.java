/*
Copyright (c) 2026, Alex McIntyre
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in the
   documentation and/or other materials provided with the distribution.
3. All advertising materials mentioning features or use of this software
   must display the following acknowledgement:
   This product includes software developed by Alex McIntyre.
4. Neither the name of the organization nor the
   names of its contributors may be used to endorse or promote products
   derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER ''AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE 
USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.mcwbalance.generics;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 * Basic Combo Box Model
 *
 * @author amcintyre
 */
public class DataComboBoxModel extends AbstractListModel implements ComboBoxModel {

    private Object selectedItem;
    String strings[];

    /**
     * Custom method used to replace entire contents of comboBox list. this
     * allows the actual list class to manage the changes
     *
     * @param newList
     */
    public void setAllData(String[] newList) {
        strings = newList;
        this.fireContentsChanged(this, 0, strings.length - 1);
    }

    /**
     * Used for finding number of options
     *
     * @return length of string array
     */
    @Override
    public int getSize() {
        return strings.length;
    }

    /**
     * For selecting a specific option from the combobox strings array
     *
     * @param index
     * @return
     */
    @Override
    public String getElementAt(int index) {
        return strings[index];
    }

    /**
     * simple pass though of selectedItem
     *
     * @return
     */
    @Override
    public Object getSelectedItem() {
        return selectedItem;
    }

    /**
     * simple pass through to selected item, no error checking
     *
     * @param newValue
     */
    @Override
    public void setSelectedItem(Object newValue) {
        selectedItem = newValue;
    }

}
