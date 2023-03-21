package com.rwAUiM.b06Ay9wD30c.utils

import android.content.res.Resources
import android.util.DisplayMetrics
import java.lang.reflect.Field

object AdaptScreenUtils {
    private var sMetricsFields: ArrayList<Field>? = null

    fun adaptWidth(resources: Resources, designWidth: Int): Resources {
        val newXdpi = resources.displayMetrics.widthPixels * 72f / designWidth
        applyDisplayMetrics(resources, newXdpi)
        return resources
    }

    /**
     * @param resources The resources.
     * @return the resource
     */
    fun closeAdapt(resources: Resources): Resources {
        val newXdpi = Resources.getSystem().displayMetrics.density * 72f
        applyDisplayMetrics(resources, newXdpi)
        return resources
    }


    private fun applyDisplayMetrics(resources: Resources, newXdpi: Float) {
        resources.displayMetrics.xdpi = newXdpi
        Utils.app.resources.displayMetrics.xdpi = newXdpi
        applyOtherDisplayMetrics(resources, newXdpi)
    }


    private fun applyOtherDisplayMetrics(resources: Resources, newXdpi: Float) {
        if (sMetricsFields == null) {
            sMetricsFields = ArrayList()
            var resCls: Class<*>? = resources.javaClass
            var declaredFields = resCls?.declaredFields
            while (declaredFields != null && declaredFields.isNotEmpty()) {
                for (field in declaredFields) {
                    if (field.type.isAssignableFrom(DisplayMetrics::class.java)) {
                        field.isAccessible = true
                        val tmpDm: DisplayMetrics? =
                            getMetricsFromField(resources, field)
                        if (tmpDm != null) {
                            sMetricsFields!!.add(field)
                            tmpDm.xdpi = newXdpi
                        }
                    }
                }
                resCls = resCls!!.superclass
                declaredFields = if (resCls != null) {
                    resCls.declaredFields
                } else {
                    break
                }
            }
        } else {
            applyMetricsFields(resources, newXdpi)
        }
    }

    private fun applyMetricsFields(resources: Resources, newXdpi: Float) {
        for (metricsField in sMetricsFields!!) {
            try {
                val dm = metricsField[resources] as DisplayMetrics
                dm.xdpi = newXdpi
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getMetricsFromField(resources: Resources, field: Field): DisplayMetrics? {
        return try {
            field[resources] as DisplayMetrics
        } catch (ignore: Exception) {
            null
        }
    }

    fun pt2Px(ptValue: Float): Int {
        val metrics: DisplayMetrics = Utils.app.resources.displayMetrics
        return (ptValue * metrics.xdpi / 72f + 0.5).toInt()
    }

    fun px2Pt(pxValue: Float): Int {
        val metrics: DisplayMetrics = Utils.app.resources.displayMetrics
        return (pxValue * 72 / metrics.xdpi + 0.5).toInt()
    }

}