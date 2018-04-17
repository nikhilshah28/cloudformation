{
	"AWSTemplateFormatVersion": "2010-09-09",
	"Description": "Setting up AD user and Workspace",
	"Parameters": {
		"WSBundleId": {
			"Description": "Please enter the Workspace Bundle ID",
			"Type": "String",
			"Default": "wsb-xxxxxx"
		},
		"WSDirectoryId": {
			"Description": "Please enter the Directory ID",
			"Type": "String",
			"Default": "d-xxxxxxxx"
		},
		"WSUserName": {
			"Description": "Please enter Active Directory UserAccount which already exists",
			"Type": "String"
		},
		"WSUserVolume": {
			"Description": "Please enter the size of User Volume",
			"Type": "String",
			"Default": "10"
		}
	},


	"Resources": {
		"workspace": {
			"Type": "AWS::WorkSpaces::Workspace",
			"Properties": {
				"BundleId": {
					"Ref": "WSBundleId"
				},
				"DirectoryId": {
					"Ref": "WSDirectoryId"
				},
				"UserName": {
					"Ref": "WSUserName"
				}

			}
		}
	}
}
