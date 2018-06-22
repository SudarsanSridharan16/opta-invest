export default {
  'id': 3,
  'name': 'crypto_7',
  'parametrization': {
    'id': 0,
    'standardDeviationMillisMaximum': 73
  },
  'regionList': [
    {
      'id': 0,
      'name': 'UK',
      'quantityMillisMaximum': 180
    },
    {
      'id': 1,
      'name': 'Global',
      'quantityMillisMaximum': 1000
    }
  ],
  'sectorList': [
    {
      'id': 0,
      'name': 'Equities',
      'quantityMillisMaximum': 400
    },
    {
      'id': 1,
      'name': 'Bonds',
      'quantityMillisMaximum': 1000
    },
    {
      'id': 2,
      'name': 'Real Estate',
      'quantityMillisMaximum': 1000
    }
  ],
  'assetClassList': [
    {
      'id': 1,
      'name': 'UK Equities',
      'regionId': 0,
      'sectorId': 0,
      'expectedReturnMillis': 100,
      'standardDeviationRiskMillis': 150,
      'correlationMillisMap': {
        '1-UK Equities': 1000,
        '2-Ex-UK Equities': 0,
        '3-Intermediate Bonds': 0,
        '4-Long-Term Bonds': 0,
        '5-UK Bonds': 0,
        '6-Real Estate': 0
      }
    },
    {
      'id': 2,
      'name': 'Ex-UK Equities',
      'regionId': 1,
      'sectorId': 0,
      'expectedReturnMillis': 80,
      'standardDeviationRiskMillis': 120,
      'correlationMillisMap': {
        '1-UK Equities': 760,
        '2-Ex-UK Equities': 1000,
        '3-Intermediate Bonds': 0,
        '4-Long-Term Bonds': 0,
        '5-UK Bonds': 0,
        '6-Real Estate': 0
      }
    },
    {
      'id': 3,
      'name': 'Intermediate Bonds',
      'regionId': 1,
      'sectorId': 1,
      'expectedReturnMillis': 40,
      'standardDeviationRiskMillis': 70,
      'correlationMillisMap': {
        '1-UK Equities': 350,
        '2-Ex-UK Equities': 40,
        '3-Intermediate Bonds': 1000,
        '4-Long-Term Bonds': 0,
        '5-UK Bonds': 0,
        '6-Real Estate': 0
      }
    },
    {
      'id': 4,
      'name': 'Long-Term Bonds',
      'regionId': 1,
      'sectorId': 1,
      'expectedReturnMillis': 45,
      'standardDeviationRiskMillis': 80,
      'correlationMillisMap': {
        '1-UK Equities': 500,
        '2-Ex-UK Equities': 300,
        '3-Intermediate Bonds': 870,
        '4-Long-Term Bonds': 1000,
        '5-UK Bonds': 0,
        '6-Real Estate': 0
      }
    },
    {
      'id': 5,
      'name': 'UK Bonds',
      'regionId': 0,
      'sectorId': 1,
      'expectedReturnMillis': 50,
      'standardDeviationRiskMillis': 90,
      'correlationMillisMap': {
        '1-UK Equities': 240,
        '2-Ex-UK Equities': 360,
        '3-Intermediate Bonds': 620,
        '4-Long-Term Bonds': 520,
        '5-UK Bonds': 1000,
        '6-Real Estate': 0
      }
    },
    {
      'id': 6,
      'name': 'Real Estate',
      'regionId': 1,
      'sectorId': 2,
      'expectedReturnMillis': 70,
      'standardDeviationRiskMillis': 100,
      'correlationMillisMap': {
        '1-UK Equities': 300,
        '2-Ex-UK Equities': 250,
        '3-Intermediate Bonds': -50,
        '4-Long-Term Bonds': -20,
        '5-UK Bonds': 200,
        '6-Real Estate': 1000
      }
    }
  ],
  'assetClassAllocationList': [
    {
      'id': 1,
      'assetClassId': 1,
      'quantityMillis': null
    },
    {
      'id': 2,
      'assetClassId': 2,
      'quantityMillis': null
    },
    {
      'id': 3,
      'assetClassId': 3,
      'quantityMillis': null
    },
    {
      'id': 4,
      'assetClassId': 4,
      'quantityMillis': null
    },
    {
      'id': 5,
      'assetClassId': 5,
      'quantityMillis': null
    },
    {
      'id': 6,
      'assetClassId': 6,
      'quantityMillis': null
    }
  ],
  'score': null
}
