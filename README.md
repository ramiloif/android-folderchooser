# android-folderchooser

A simple alert dialog allows the user to choose a directory .

## Download 
``` Groovy
compile 'com.io.tools.android.ramiloif.folderchooser:folderchooser-dialog:1.0.6'
```
## Usage
* Create a dialog using builder
```java
ChooseDirectoryDialog dialog =
  ChooseDirectoryDialog.builder(MainActivity.this). // Context
  titleText("Choose directory"). // The title will be shown
  startDir(Environment.getExternalStorageDirectory().getAbsoluteFile()).// File from where to start
  showNeverAskAgain(true). // Enable or disable 'Never ask again checkbox
  neverAskAgainText("Never ask again"). // Text of never ask again check box(if enabled)
  onPickListener(new ChooseDirectoryDialog.DirectoryChooseListener() {
      @Override
      public void onDirectoryPicked(ChooseDirectoryDialog.DialogResult result) {
          // Listener to users choice
          // result.getPath() - The path of the folder picked by user
          // result.isAskAgain()  - Did the user checked the 'Never ask again' Checkbox if true it means never ask again
          resultTV.setText("You picked \n " + 
          result.getPath() +
          "\n Never ask again = " +
          result.isAskAgain());
      }

      @Override
      public void onCancel() {
          resultTV.setText("operation canceled");
      }
  }).build();
dialog.show();
```

## Requirements
Read external storage permission
```xml
  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```
