/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.radiant.messaging.ui.mediapicker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.radiant.messaging.FakeFactory;
import com.radiant.messaging.R;
import com.radiant.messaging.datamodel.FakeDataModel;
import com.radiant.messaging.datamodel.binding.Binding;
import com.radiant.messaging.datamodel.binding.BindingBase;
import com.radiant.messaging.datamodel.data.DraftMessageData;
import com.radiant.messaging.datamodel.data.MediaPickerData;
import com.radiant.messaging.ui.FragmentTestCase;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;

public class MediaPickerTest extends FragmentTestCase<MediaPicker> {
    @Mock protected MediaPickerData mMockMediaPickerData;
    @Mock protected DraftMessageData mMockDraftMessageData;
    protected FakeDataModel mFakeDataModel;

    public MediaPickerTest() {
        super(MediaPicker.class);
    }

    @Override
    protected MediaPicker getFragment() {
        if (mFragment == null) {
            mFragment = new MediaPicker(getInstrumentation().getTargetContext());
        }
        return mFragment;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final Context context = getInstrumentation().getTargetContext();
        mFakeDataModel = new FakeDataModel(context)
                   .withMediaPickerData(mMockMediaPickerData);
        FakeFactory.register(context)
                   .withDataModel(mFakeDataModel);
    }

    /**
     * Helper method to initialize the MediaPicker and its data.
     */
    private void initFragment(final int supportedMediaTypes, final Integer[] expectedLoaderIds,
            final boolean filterTabBeforeAttach) {
        Mockito.when(mMockMediaPickerData.isBound(Matchers.anyString()))
            .thenReturn(true);
        Mockito.when(mMockDraftMessageData.isBound(Matchers.anyString()))
            .thenReturn(true);
        final Binding<DraftMessageData> draftBinding = BindingBase.createBinding(this);
        draftBinding.bind(mMockDraftMessageData);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final MediaPicker fragment = getFragment();
                if (filterTabBeforeAttach) {
                    fragment.setSupportedMediaTypes(supportedMediaTypes);
                    getActivity().setFragment(fragment);
                } else {
                    getActivity().setFragment(fragment);
                    fragment.setSupportedMediaTypes(supportedMediaTypes);
                }
                fragment.setDraftMessageDataModel(draftBinding);
                Mockito.verify(mMockMediaPickerData,
                        Mockito.atLeastOnce()).init(
                        Matchers.eq(fragment.getLoaderManager()));
                fragment.open(MediaPicker.MEDIA_TYPE_ALL, false);
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    public void testDefaultTabs() {
        Mockito.when(mMockMediaPickerData.getSelectedChooserIndex()).thenReturn(0);
        initFragment(MediaPicker.MEDIA_TYPE_ALL, new Integer[] {
                MediaPickerData.GALLERY_MEDIA_LOADER },
                false);
        final MediaPicker mediaPicker = getFragment();
        final View view = mediaPicker.getView();
        assertNotNull(view);
        final ViewGroup tabStrip = (ViewGroup) view.findViewById(R.id.mediapicker_tabstrip);
        assertEquals(tabStrip.getChildCount(), 4);
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            final ImageButton tabButton = (ImageButton) tabStrip.getChildAt(i);
            assertEquals(View.VISIBLE, tabButton.getVisibility());
            assertEquals(i == 0, tabButton.isSelected());
        }
    }

    public void testFilterTabsBeforeAttach() {
        Mockito.when(mMockMediaPickerData.getSelectedChooserIndex()).thenReturn(0);
        initFragment(MediaPicker.MEDIA_TYPE_IMAGE, new Integer[] {
                MediaPickerData.GALLERY_MEDIA_LOADER },
                true);
        final MediaPicker mediaPicker = getFragment();
        final View view = mediaPicker.getView();
        assertNotNull(view);
        final ViewGroup tabStrip = (ViewGroup) view.findViewById(R.id.mediapicker_tabstrip);
        assertEquals(tabStrip.getChildCount(), 4);
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            final ImageButton tabButton = (ImageButton) tabStrip.getChildAt(i);
            assertEquals(i == 0, tabButton.isSelected());
        }
    }
}
