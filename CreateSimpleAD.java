{
	"AWSTemplateFormatVersion": "2010-09-09",
	"Description": "Create Simple Active Directory",
	"Parameters": {
		"FQDN": {
			"Description": "Enter the Fully Qualified Domain Name, such as corp.example.com",
			"Type": "String"
		},
		"AdminPwd": {
			"Description": "Enter the password for the directory Admin user",
			"Type": "String"
		},
		"Netbios": {
			"Description": "Enter the Netbios short name, such as CORP",
			"Type": "String"
		},
		"ADSize": {
			"Description": "Please enter the size of the directory",
			"Type": "String",
			"Default": "Small"
		},
		"vpcID": {
			"Description": "Please enter VPC ID",
			"Type": "String",
			"Default": "vpc-bc758dc7"
		},
		"PrivateSubnet1": {
			"Description": "Please enter the private subnet 1",
			"Type": "String",
			"Default": "subnet-97cf8fca"
		},
		"PrivateSubnet2": {
			"Description": "Please enter the private subnet 2",
			"Type": "String",
			"Default": "subnet-20bcf00f"
		}
	},


	"Resources": {
		"vdiDirectoryService": {
			"Type": "AWS::DirectoryService::SimpleAD",
			"Properties": {
				"Description": "DriveWealth VDI",
				"Name": {
					"Ref": "FQDN"
				},
				"Password": {
					"Ref": "AdminPwd"
				},
				"Size": {
					"Ref": "ADSize"
				},
				"VpcSettings": {
					"SubnetIds": [{
						"Ref": "PrivateSubnet1"
					}, {
						"Ref": "PrivateSubnet2"
					}],
					"VpcId": {
						"Ref": "vpcID"
					}
				}
			}

		}
	}

}
