package com.drawshtuff.test.runner;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;

import java.io.File;
import java.lang.reflect.Method;

import org.junit.runners.model.InitializationError;
import org.mockito.MockitoAnnotations;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.RobolectricTestRunner;
import com.xtremelabs.robolectric.shadows.ShadowDialog;
import com.xtremelabs.robolectric.shadows.ShadowInputMethodManager;

public class CoPilotTestRunner extends RobolectricTestRunner {

	public CoPilotTestRunner(Class<?> testClass) throws InitializationError {
		super(testClass, new File("../scooterapp"));
	}

	@Override
	public Object createTest() throws Exception {
		Object theTest = super.createTest();
		MockitoAnnotations.initMocks(theTest);
		return theTest;
	}

	@Override
	public void afterTest(Method method) {
		super.afterTest(method);
	}

	@Override
	protected void bindShadowClasses() {
	}

	public static ShadowInputMethodManager getInputMethodManager() {
		return shadowOf((InputMethodManager) systemService(Context.INPUT_METHOD_SERVICE));
	}

	@SuppressWarnings("unchecked")
	public static <ServiceType> ServiceType systemService(final String serviceName) {
		return (ServiceType) Robolectric.application.getSystemService(serviceName);
	}

	@SuppressWarnings("unchecked")
	public static <DialogType extends ShadowDialog> DialogType lastDialog() {
		return (DialogType) Robolectric.getShadowApplication().getLatestDialog();
	}
}
