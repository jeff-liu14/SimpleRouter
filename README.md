
[![Hex.pm](https://img.shields.io/hexpm/l/plug.svg)](https://www.apache.org/licenses/LICENSE-2.0)

# SimpleRouter


```
    Android组件化路由库，支持模块间通信
```

--- 

### 最新版本 

模块|srouter-annotation|srouter-compiler|srouter-api
---|---|---|---
版本 | [![](https://jitpack.io/v/jeff-liu14/srouter-annotation.svg)](https://jitpack.io/#jeff-liu14/srouter-annotation) | [![](https://jitpack.io/v/jeff-liu14/srouter-compiler.svg)](https://jitpack.io/#jeff-liu14/srouter-compiler) | [![](https://jitpack.io/v/jeff-liu14/srouter-api.svg)](https://jitpack.io/#jeff-liu14/srouter-api)

### Demo展示

##### [Demo apk下载](https://github.com/jeff-liu14/SimpleRouter/blob/main/demo/SimpleRouter_V1.0_release.apk)

#### <img src="https://github.com/jeff-liu14/SimpleRouter/blob/main/demo/demo.gif" width="300"/> 


---
### 接入方式
1. 在build.gradle文件中添加依赖和配置

	参数示意：

	```
	> SIMPLE_ROUTER_KEY   ->   配置生成的路由文件加密秘钥，长度为16个字
	> OPEN_AES            ->   是否开启加密
	> SROUTER_ANNOTATION  ->   annotation版本号(查看上面最新版本)
	> SROUTER_API         ->   api版本号(查看上面最新版本)
	> SROUTER_COMPILER    ->   compiler版本号(查看上面最新版本)
	```	
	
	代码配置：
	
	```groovy
	
	    plugins {
		    id 'kotlin-kapt'
	    }
	    
	    android {
	        defaultConfig {
	            javaCompileOptions {
	                annotationProcessorOptions {
	                    arguments = [
	                            SIMPLE_ROUTER_KEY: project.ext.simpleRouterKey,
	                            OPEN_AES         : project.ext.openAes
	                    ]
	                }
	            }
	        }
	    }
	
	    dependencies {
	        implementation "com.github.jeff-liu14:srouter-annotation:$SROUTER_ANNOTATION"
	        implementation "com.github.jeff-liu14:srouter-api:$SROUTER_API"
	        kapt "com.github.jeff-liu14:srouter-compiler:$SROUTER_COMPILER"
	    }
	```

2. 在需要的Activity/Fragment中添加注解

    ``` kotlin
    #activity
    @Route(path = "/app/home")
	class MainActivity : AppCompatActivity() {} 	
	 	
	# fragment
	@Route(path = "/app/demo/product/fragment")
	class ProductFragment : Fragment() {}
    ```

3. 初始化SDK

    ``` kotlin
       SimpleRouter.init(this)
       SimpleRouter.scanRoute(BuildConfig.SIMPLE_ROUTER_KEY, BuildConfig.OPEN_AES)
    ```

4. 路由跳转操作

    ``` kotlin
    //无参跳转
   SimpleRouter.getInstance()
                .build("/app/demo/profile")
                .navigate(this)

    //带参跳转
    SimpleRouter.getInstance()
                .build("/app/demo/profile")
                .withString("name", "app-profile:透传参数")
                .navigate(this)   
                
    ```		

5. 混淆(Proguard)规则

    ``` 
    #FastJson
	-dontwarn com.alibaba.fastjson.**
	-keep class com.alibaba.fastjson.**{*; }
	#SimpleRouter
	-keep class * extends com.laydown.srouter.api.provider.IProvider
    ```
  
---

### API详解
具体使用方法可参考示例

1. 手动加载路由文件

	```kotlin
	SimpleRouter.getInstance()
	            .scanRoute(BuildConfig.SIMPLE_ROUTER_KEY, BuildConfig.OPEN_AES)
	
	```		

2. 无参跳转方法
	
	```kotlin
	SimpleRouter.getInstance()
	            .build("/app/demo/profile")
	            .navigate(this)
	```

3. 有参构造方法

	```kotlin
	// 基础数据类型 String boolean int long float
	SimpleRouter.getInstance()
	            .build("/app/demo/profile")
	            .withString("name", "app-profile:透传参数")
	            .navigate(this)
	 // 使用Bundle
	 SimpleRouter.getInstance()
	            .build("/app/demo/profile")
	            .withBundle(Bundle().apply {
	                putString("name", "app-profile:透传参数")
	            })
	            .navigate(this)
	``` 
4. startActivityForResult

	```kotlin
	// 旧版跳转
	SimpleRouter.getInstance()
                .build("/app/demo/product")
                .withString("name", "app-product:透传参数")
                .navigateForResult(this, 10001)
                
   // 使用ActivityResultLauncher
   val launcher: ActivityResultLauncher<Intent> =
            Helper.startActivityForResult(this) { result ->
                when (result?.resultCode) {
                    RESULT_OK -> {
                        Toast.makeText(
                            this, result.data?.extras?.getString("uName"), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            
    // 使用SimpleRouter获取构造的intent
    SimpleRouter.getInstance()
                .build("/app/demo/product")
                .withString("name", "app-product:透传参数")
                .navigateForResultX(this, launcher)	
   ```
	
5. 获取Fragment

	```kotlin
	 private fun setFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        val fragment =
            SimpleRouter.getInstance()
                .build("/app/demo/product/fragment")
                .withString("name", intent.extras?.getString("name"))
                .navigate() as Fragment
        fragment.arguments = intent.extras

        transaction.add(R.id.fl_content, fragment, fragment.javaClass.simpleName)
        transaction.commitNowAllowingStateLoss()
    }
	```
	
6. 使用provider实现module对外发布ability

	```kotlin
	//在lib-provider中定义biz-tax模块对外发布的接口
	public interface ITaxProvider extends IProvider {
   	 	void sayHello(String message);
	}
	
	/**
	 * 在biz-tax模块中实现ITaxProvider接口
	 * 并且使用@Route标签注册服务
	 */
	@Route(path = "/tax/provider")
	public class TaxProviderImpl implements ITaxProvider {
	    private Context mContext;
	
	    @Override
	    public void sayHello(String message) {
	        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
	    }
	
	    @Override
	    public void init(Context context) {
	        this.mContext = context;
	    }
	}
	
	//在biz-shop模块中获取服务并调用
	 val provider: ITaxProvider = SimpleRouter.getInstance()
                    .build("/tax/provider")
                    .navigate(this@ShopActivity) as ITaxProvider
    provider.sayHello("ShopActivity call Tax Provider")
	```

7. 全局降级策略
	
	```kotlin
	//实现全局降级策略服务接口DegradeService
	class CommonDegradeImpl : DegradeService {
		    override fun onLost(context: Context, targetMeta: TargetMeta) {
		        Toast.makeText(context, "Path: " + targetMeta.path + " Lost.", Toast.LENGTH_SHORT).show()
		    }
	}
	
	//在SimpleRouter中进行注册
	SimpleRouter.setDegradeService(CommonDegradeImpl())
	
	```
8. 全局拦截策略

	```kotlin
	//实现全局拦截策略服务接口InterceptorCallBack
	//return false-页面跳转被拦截 true-继续进行路由操作
	class CommonInterceptorImpl : InterceptorCallBack {
	    override fun onContinue(context: Context, targetMeta: TargetMeta): Boolean {
	        when (targetMeta.path) {
	            "/app/demo/product1" -> {
	                Toast.makeText(context, "Product页面被拦截", Toast.LENGTH_SHORT).show()
	                return false
	            }
	            "/app/demo/profile" -> {
	                Toast.makeText(context, "Profile页面被拦截", Toast.LENGTH_SHORT).show()
	                return false
	            }
	        }
	        return true
	    }
}
//在SimpleRouter中注册	
SimpleRouter.setInterceptorCallBack(CommonInterceptorImpl())
	
	```