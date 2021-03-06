package com.appsnipp.loginsamples.utils;

import android.content.Context;

import com.appsnipp.loginsamples.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;

public class  ChipItem {

    //    private val globalApplication = GlobalApplication.appContext!!
    public Chip getChip(Context context, String text, String tag) {
        Chip chip = new Chip(context);
        chip.setChipDrawable(ChipDrawable.createFromResource(context, R.xml.chip_item));
//        Padding paddingDp = TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP, 10f,
//                context.resources.displayMetrics
//        ).toInt()
//        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
        chip.setText(text);
//        chip.setOnCloseIconClickListener { entryChipGroup.removeView(chip) }
        chip.setTag(tag);
        return chip;
    }
}