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

import com.mcwbalance.settings.Limit;
import com.mcwbalance.settings.Preferences;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * used for storing a time series that can be looked up by day, where the time
 * step may not match the index value
 *
 * @author ALex McIntyre
 */
public class DataTimeIntSeries {

    /**
     * length of data in series, note that length of array can be resized and
     * does not match the length of data, so this length value should be used
     * instead
     */
    public int length;
    private int[] step;
    private int[] value;
    /**
     * Used for limiting the length of the data series
     */
    public static int MAX_LENGTH = Limit.MAX_LEVELS; // maximum length of DAC that will be accepted,  intention is about 1000 or 1500 to allow for 150 m tall TSF 0.1 m increments. 
    /**
     * Used for identification of null value
     */
    public static final int DAY_NULL = -1;
    /**
     * Used for identification of null value
     */
    public static final int VAL_NULL = -1;

    /**
     * constructs a blank null value array, mostly as a placeholder for table
     * generation, note that calling remove DoupAndDUlls will erase the list.
     *
     * @param nPoints
     */
    public DataTimeIntSeries(int nPoints) {
        if (nPoints > MAX_LENGTH) {
            nPoints = MAX_LENGTH;
        } else if (nPoints < 1) {
            nPoints = 1;
        }
        length = nPoints;
        step = new int[length];
        value = new int[length];
        step[0] = 0; // first day mmust be 0; 
        value[0] = 0; // note program will ignore the first DAC area and storage given as they must be 0;
        for (int i = 1; i < nPoints; i++) {
            step[i] = DAY_NULL;
            value[i] = VAL_NULL;
        }
    }

    /**
     * constructs the full set of both arrays, as well calls removeDoupAndNulls
     * and sortAssending Methods
     *
     * @param inDay
     * @param inValue
     */
    public DataTimeIntSeries(int inDay[], int inValue[]) {
        if (inDay.length < 1 || inValue.length < 1) {
            length = 1;
            step = new int[length];
            value = new int[length];
            step[0] = 0;
            value[0] = 0;
        } else if (inDay.length == inValue.length) {
            if (inDay.length <= MAX_LENGTH) {
                step = inDay;
                value = inValue;
            } else {
                length = MAX_LENGTH;
                step = new int[length];
                value = new int[length];
                for (int i = 0; i < length; i++) {
                    step[i] = inDay[i];
                    value[i] = inValue[i];
                }
            }

        } else if (inDay.length < inValue.length) {
            length = inDay.length;
            step = new int[length];
            value = new int[length];
            for (int i = 0; i < length; i++) {
                step[i] = inDay[i];
                value[i] = inValue[i];
            }
        } else {
            length = inValue.length;
            step = new int[length];
            value = new int[length];
            for (int i = 0; i < length; i++) {
                step[i] = inDay[i];
                value[i] = inValue[i];
            }
        }
        sortAssending();
        removeDoupAndNulls();
    }

    /**
     * Used for getting a save file formatted XML element to append into a
     * larger doc Only appends if length > 0
     *
     * @see getXMLElement
     * @param element Element to append too
     * @param xMLDoc
     * @param tagname
     */
    public void appendXMLElement(Element element, Document xMLDoc, String tagname) {
        if (length > 0) {
            element.appendChild(getXMLElement(xMLDoc, tagname));
        }
    }

    /**
     * used for getting array of timesteps
     *
     * @return array of timestep values
     */
    public int[] getDays() {
        return step;
    }

    /**
     * Simple pass through of value array
     *
     * @return value array
     */
    public int[] getValues() {
        return value;
    }

    /**
     * used for looking up value
     *
     * @param step index step to look for value
     * @return corresponding value, or value at step[0] if no match is found
     */
    public int getValue(int step) {
        for (int i = length - 1; i > -1; i--) {
            if (step >= this.step[i]) {
                return value[i];
            }
        }
        System.err.println("DataTimeIntSeries - WARNING VALUE NOT FOUND FOR SEARCHED DAY " + step + " VALUE OF " + value[0] + "RETURNED INSTEAD");
        return value[0];
    }

    /**
     *
     * @param header
     * @return
     */
    public StringBuilder getTabbedString(String header) {
        StringBuilder tabbedString = new StringBuilder();
        tabbedString.append(header);
        tabbedString.append(System.getProperty("line.separator"));
        for (int i = 0; i < length; i++) {
            tabbedString.append(step[i]);
            tabbedString.append("\\t");
            tabbedString.append(value[i]);
            tabbedString.append(System.getProperty("line.separator"));
        }
        tabbedString.append(Preferences.LIST_TERMINATOR);
        tabbedString.append(System.getProperty("line.separator"));
        return tabbedString;
    }

    /**
     * Used for getting a save file formatted XML element to append into a
     * larger doc
     *
     * @param xMLDoc Document required to generate element,
     * @param tagname Name of element
     * @return
     */
    private Element getXMLElement(Document xMLDoc, String tagname) {
        Element ele = xMLDoc.createElement(tagname);
        for (int i = 0; i < length; i++) {
            Element cele = xMLDoc.createElement("row");
            cele.setAttribute("Index", String.valueOf(step[i]));
            cele.setAttribute("Name", String.valueOf(value[i]));
            ele.appendChild(cele);
        }
        return ele;
    }

    /**
     *
     * @param line
     * @param index
     */
    public void setFromStringLine(String line, int index) {
        if (index < MAX_LENGTH && index >= 0 && line != Preferences.LIST_TERMINATOR) {
            if (index + 1 < length) {
                int newdayArray[] = new int[index + 1];
                int newvalueArray[] = new int[index + 1];
                for (int i = 0; i < length; i++) {
                    newdayArray[i] = step[i];
                    newvalueArray[i] = value[i];
                }
                int newday = Integer.valueOf(line.split("\\t")[0]);
                int newvalue = Integer.valueOf(line.split("\\t")[1]);
                newdayArray[index + 1] = newday;
                newvalueArray[index + 1] = newvalue;
                step = newdayArray;
                value = newvalueArray;
            } else { // needed for the first row.
                int newday = Integer.valueOf(line.split("\\t")[0]);
                int newvalue = Integer.valueOf(line.split("\\t")[1]);
                step[index + 1] = newday;
                value[index + 1] = newvalue;
            }
        }
    }

    /**
     *
     * @param inDay
     * @param inValue
     */
    public void setAllData(int inDay[], int inValue[]) {
        int newDay[];
        int newValue[];
        if (inDay.length < 1 || inValue.length < 1) {
            length = 1;
            newDay = new int[length];
            newValue = new int[length];
            newDay[0] = 0;
            newValue[0] = 0;
        } else if (inDay.length == inValue.length) {
            if (inDay.length <= MAX_LENGTH) {
                length = inDay.length;
                newDay = inDay;
                newValue = inValue;
            } else {
                length = MAX_LENGTH;
                newDay = new int[length];
                newValue = new int[length];
                for (int i = 0; i < length; i++) {
                    newDay[i] = inDay[i];
                    newValue[i] = inValue[i];
                }
            }

        } else if (inDay.length < inValue.length) {
            length = inDay.length;
            newDay = new int[length];
            newValue = new int[length];
            for (int i = 0; i < length; i++) {
                newDay[i] = inDay[i];
                newValue[i] = inValue[i];
            }
        } else {
            length = inValue.length;
            newDay = new int[length];
            newValue = new int[length];
            for (int i = 0; i < length; i++) {
                newDay[i] = inDay[i];
                newValue[i] = inValue[i];
            }
        }
        //day = new int[length];
        //value = new int[length];

        step = newDay;
        value = newValue;
        sortAssending();
        removeDoupAndNulls();
    }

    /**
     * Removes any douplicate day values since 1 day should not have 2 separate
     * operating targets Also removes null values from list (i.e. -1) to
     * minimize clutter
     */
    public final void removeDoupAndNulls() { // this is having the problem
        double cDay;
        for (int i = 0; i < length; i++) {
            cDay = step[i];
            for (int j = i + 1; j < length; j++) {
                if (step[j] == cDay || step[j] == DAY_NULL || value[j] == VAL_NULL) {
                    for (int k = j + 1; k < length; k++) {
                        step[k - 1] = step[k];
                        value[k - 1] = value[k];
                    }
                    length--;
                }
            }
        }
        if (length != step.length) {
            int newDay[] = new int[length];
            int newValue[] = new int[length];
            newDay[0] = 0; // forces first day value to be 0;
            newValue[0] = value[0];

            for (int i = 1; i < length; i++) {
                newDay[i] = step[i];
                newValue[i] = value[i];
            }
            step = newDay;
            value = newValue;
        }
    }

    /**
     * Sorts both day and Value arrays in ascending order of day;
     */
    public final void sortAssending() {
        int swapDay;
        int swapValue;
        for (int i = 0; i < length; i++) { // may need to move the i++ and j++, want to repeat the step if needed;
            for (int j = i + 1; j < length; j++) {
                if (step[j] < step[i]) { // if it matches leave it alone, the doublicate cleaner will deal with it; 
                    swapDay = step[j];
                    swapValue = value[j];
                    for (int k = j - 1; k > i - 1; k--) {
                        step[k + 1] = step[k];
                        value[k + 1] = value[k];
                    }
                    step[i] = swapDay;
                    value[i] = swapValue;
                    i = -1; // assumes i++ will bring this back to 0 and loop will restart;
                    break;
                }
            }
        }
    }

}
