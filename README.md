# NTK Android Estate - اپلیکیشن املاک اندروید

## 📱 درباره پروژه

این پروژه یک اپلیکیشن اندروید برای مدیریت املاک است که با استفاده از Java و Android SDK توسعه یافته است. اپلیکیشن امکانات مختلفی برای مدیریت املاک، اخبار، مقالات و سفارشات مشتریان ارائه می‌دهد.

## 🏗️ معماری پروژه

### ساختار کلی
- **Package Name**: `ntk.android.estate`
- **Application ID**: `ntk.android.estate.APPNTK`
- **Version**: 2.01
- **Target SDK**: 34
- **Min SDK**: 21
- **Java Version**: 17

### اجزای اصلی

#### 1. Activities (فعالیت‌ها)
- **SplashActivity**: صفحه شروع اپلیکیشن
- **MainActivity**: صفحه اصلی با نمایش املاک
- **EstateListActivity**: لیست املاک
- **EstateDetailActivity**: جزئیات ملک
- **NewEstateActivity**: ثبت ملک جدید
- **MyEstateActivity**: املاک کاربر
- **FavoriteEstateListActivity**: لیست علاقه‌مندی‌ها
- **NewsListActivity**: لیست اخبار
- **ArticleListActivity**: لیست مقالات
- **ProfileActivity**: پروفایل کاربر

#### 2. Fragments (فرگمنت‌ها)
- **NewEstateFragment1-5**: مراحل ثبت ملک جدید
- **NewOrderFragment1-4**: مراحل ثبت سفارش جدید
- **EditEstateFragment1,5**: ویرایش ملک

#### 3. Adapters (آداپترها)
- **EstatePropertyAdapter**: آداپتر املاک
- **NewsAdapter**: آداپتر اخبار
- **ArticleAdapter**: آداپتر مقالات
- **CompanyAdapter**: آداپتر شرکت‌ها
- **ProjectAdapter**: آداپتر پروژه‌ها

#### 4. Services (سرویس‌ها)
- **SmsService**: سرویس دریافت پیامک

## 🛠️ تکنولوژی‌های استفاده شده

### کتابخانه‌های اصلی
- **AndroidX Core**: 1.15.0
- **Material Design**: 1.12.0
- **ConstraintLayout**: 2.2.0
- **Lifecycle Components**: 2.8.7
- **Room Database**: 2.6.1
- **Firebase**: 33.6.0
- **ExoPlayer**: 1.4.1
- **Google Play Services**: 21.3.0

### کتابخانه‌های UI
- **Lottie**: انیمیشن‌های Lottie
- **Universal Image Loader**: بارگذاری تصاویر
- **FlowingDrawer**: منوی کشویی
- **BubbleNavigation**: ناوبری حبابی
- **PageIndicatorView**: نشانگر صفحات

### کتابخانه‌های نقشه
- **Mapir SDK**: نقشه‌های ایرانی

## 📋 ویژگی‌های اپلیکیشن

### 🏠 مدیریت املاک
- نمایش لیست املاک
- جستجوی پیشرفته املاک
- ثبت ملک جدید
- ویرایش املاک موجود
- مدیریت املاک کاربر
- لیست علاقه‌مندی‌ها

### 📰 اخبار و مقالات
- نمایش اخبار
- جستجوی اخبار
- نمایش مقالات
- جستجوی مقالات
- دسته‌بندی محتوا

### 👤 مدیریت کاربر
- پروفایل کاربر
- ورود و ثبت‌نام
- مدیریت سفارشات

### 🏢 شرکت‌ها و پروژه‌ها
- لیست شرکت‌ها
- جزئیات شرکت
- لیست پروژه‌ها
- جزئیات پروژه

## 🔧 تنظیمات پروژه

### مجوزهای مورد نیاز
```xml
- INTERNET
- ACCESS_NETWORK_STATE
- RECEIVE_SMS
- READ_MEDIA_IMAGES
- READ_MEDIA_AUDIO
- READ_MEDIA_VIDEO
- READ_EXTERNAL_STORAGE
- WRITE_EXTERNAL_STORAGE
- VIBRATE
```

### تنظیمات Firebase
- Firebase Messaging برای اعلان‌ها
- Sentry برای ردیابی خطاها

### تنظیمات نقشه
- Mapir SDK برای نقشه‌های ایرانی
- Google Play Services برای موقعیت‌یابی

## 🚀 نحوه اجرا

### پیش‌نیازها
- Android Studio Arctic Fox یا بالاتر
- JDK 17
- Android SDK 34
- Gradle 8.0.2

### مراحل نصب
1. کلون کردن پروژه
2. باز کردن پروژه در Android Studio
3. همگام‌سازی Gradle
4. اجرای پروژه

### تنظیمات کلید
```gradle
// تنظیمات کلید در build.gradle
signingConfigs {
    release {
        storeFile file('../../keys/key.jks')
        storePassword "APPstorePassword"
        keyAlias "APPkeyAlias"
        keyPassword "APPkeyPassword"
    }
}
```

## 📱 صفحات اصلی

### صفحه شروع (SplashActivity)
- انیمیشن Lottie
- نمایش نسخه اپلیکیشن
- راه‌اندازی اولیه

### صفحه اصلی (MainActivity)
- نمایش املاک در کاروسل
- منوی کشویی
- دسترسی به بخش‌های مختلف

### مدیریت املاک
- **EstateListActivity**: لیست کامل املاک
- **EstateDetailActivity**: جزئیات کامل ملک
- **SearchEstateActivity**: جستجوی پیشرفته
- **NewEstateActivity**: ثبت ملک جدید

## 🎨 طراحی UI

### تم‌های استفاده شده
- **AppTheme**: تم اصلی Material Design
- **Theme.UserDialog**: تم دیالوگ کاربر
- **Theme.AppCompat.Light.NoActionBar.FullScreen**: تم تمام صفحه

### رنگ‌بندی
- رنگ‌های اصلی در `colors.xml`
- پشتیبانی از RTL
- فونت‌های فارسی

## 📊 ساختار داده

### مدل‌های اصلی
- **EstatePropertyModel**: مدل ملک
- **NewsModel**: مدل اخبار
- **ArticleModel**: مدل مقالات
- **CompanyModel**: مدل شرکت
- **ProjectModel**: مدل پروژه

## 🔒 امنیت

### تنظیمات امنیتی
- ProGuard برای مبهم‌سازی کد
- امضای دیجیتال برای انتشار
- رمزگذاری داده‌های حساس

## 📱 پشتیبانی از زبان‌ها

- فارسی (پیش‌فرض)
- انگلیسی
- آلمانی

## 🐛 رفع خطا و مانیتورینگ

### Sentry Integration
```java
// تنظیمات Sentry در MyApplication
<meta-data
    android:name="io.sentry.dsn"
    android:value="https://9ca63cfdc64341d9b9b95ca0f41b52bc@o1135344.ingest.sentry.io/6232546" />
```

## 📈 عملکرد

### بهینه‌سازی‌ها
- MultiDex برای اپلیکیشن‌های بزرگ
- ImageLoader برای مدیریت تصاویر
- Room Database برای ذخیره‌سازی محلی
- RecyclerView برای لیست‌های بهینه

## 🤝 مشارکت

برای مشارکت در پروژه:
1. Fork کردن پروژه
2. ایجاد branch جدید
3. اعمال تغییرات
4. ارسال Pull Request

## 📄 مجوز

این پروژه تحت مجوز [نام مجوز] منتشر شده است.

## 📞 تماس

- **تلفن**: +9810005612020
- **ایمیل**: [ایمیل تماس]
- **وب‌سایت**: [آدرس وب‌سایت]

---

**نکته**: این اپلیکیشن برای استفاده در بازار املاک ایران طراحی شده و از سرویس‌های محلی مانند Mapir استفاده می‌کند.
