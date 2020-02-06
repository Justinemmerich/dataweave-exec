package com.example.dataweave;

import org.mule.runtime.api.el.BindingContext;
import org.mule.runtime.api.metadata.DataType;
import org.mule.runtime.api.metadata.MediaType;
import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.api.streaming.CursorProvider;
import org.mule.runtime.core.api.util.IOUtils;
import org.mule.runtime.core.internal.el.DefaultBindingContextBuilder;
import org.mule.weave.v2.el.WeaveExpressionLanguage;

import java.io.InputStream;

public class Exec {


    private  WeaveExpressionLanguage weaveEngine;

    public final String PAYLOAD = "payload";

    public  final String BASE_TRANS = "%dw 2.0\n" +
            "output application/json\n" +
            "---\n" +
            "payload.hello";

    public  final String EXAMPLE = "{\n" +
            "    \"hello\" : \"world\"\n" +
            "}";

    public  final String APPLICATION_JSON = "application/json";

    private String getTypedValueStringValue(TypedValue<?> evaluate) {
        Object value = evaluate.getValue();
        if(value instanceof CursorProvider){
            value = ((CursorProvider) value).openCursor();
        }
        String textToShow;
        if(value instanceof InputStream){
            textToShow = IOUtils.toString((InputStream) value);
        } else {
            textToShow = value.toString();
        }
        return textToShow;
    }

    private DataType getInputDataType(String mimeType) {
        return DataType.builder()
                .type(String.class)
                .mediaType(MediaType.parse(mimeType))
                .build();
    }

    public void run() {
        System.out.println("run");

        weaveEngine = new WeaveExpressionLanguage();

        TypedValue inputTypedValue = new TypedValue<String>(EXAMPLE, getInputDataType(APPLICATION_JSON));

        BindingContext bindingContext = new DefaultBindingContextBuilder()
                .addBinding(PAYLOAD, inputTypedValue)
                .build();

        TypedValue<?> evaluate = weaveEngine.evaluate(BASE_TRANS, bindingContext);
        System.out.println("原文");
        System.out.println(EXAMPLE);
        System.out.println(APPLICATION_JSON);
        System.out.println("公式");
        System.out.println(BASE_TRANS);
        System.out.println("结果");
        System.out.println(getTypedValueStringValue(evaluate));
        System.out.println(evaluate.getDataType().getMediaType());

    }

}
