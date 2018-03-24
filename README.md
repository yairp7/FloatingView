# FloatingView
An abstract draggable floating view class to show on the android screen(like facebook heads)

Instructions
------------

<b>Setup:</b>
```
windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
viewLayoutParams = new WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.TYPE_PHONE,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        PixelFormat.TRANSLUCENT);
        
specialView = new SpecialView(this);
```
<b>Show:</b>
```
viewLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
viewLayoutParams.x = ravenView.getGap();
viewLayoutParams.y = ravenView.getGap();
windowManager.addView(specialView, viewLayoutParams);
```
* Requires android.permission.SYSTEM_ALERT_WINDOW permission
