package com.laydown.srouter.compiler;

import com.alibaba.fastjson.JSONObject;
import com.google.auto.service.AutoService;
import com.laydown.srouter.annotation.Route;
import com.laydown.srouter.compiler.util.FileUtil;
import com.laydown.srouter.compiler.util.ProcessorTool;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import static com.laydown.srouter.compiler.util.Const.OPEN_AES;
import static com.laydown.srouter.compiler.util.Const.SIMPLE_ROUTER_KEY;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes({"com.laydown.srouter.annotation.Route"})
public class SimpleRouterProcessor extends AbstractProcessor {
    private Messager messager;
    private Filer filer;
    private Types types;
    private Elements elementUtils;
    String simpleRouterKey = null;
    boolean openAes = false;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "enter init.....");
        filer = processingEnv.getFiler();
        types = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        Map<String, String> options = processingEnv.getOptions();
        if (MapUtils.isNotEmpty(options)) {
            simpleRouterKey = options.get(SIMPLE_ROUTER_KEY);
            if (StringUtils.isNotEmpty(options.get(OPEN_AES))) {
                openAes = Boolean.parseBoolean(options.get(OPEN_AES));
                messager.printMessage(Diagnostic.Kind.NOTE, "\nsimpleRouterKey -> " + simpleRouterKey + "\nopenAes -> " + openAes);
            }
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Route.class);
        if (!elements.isEmpty()) {
            HashMap<String, JSONObject> destMap = new HashMap<>();
            ProcessorTool.handleDestination(elementUtils, types, messager, elements, Route.class, destMap);
            FileUtil.saveIntoAsset(filer, messager, openAes, simpleRouterKey, destMap);
        }
        return false;
    }
}
