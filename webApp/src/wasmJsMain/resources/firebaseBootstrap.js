/**
 * Thin Firebase web facade for Kotlin/Wasm interop.
 * Expects Firebase compat SDK globals (firebase-app/auth/firestore-compat).
 */
(function (global) {
  "use strict";

  var auth = null;
  var db = null;

  function ensureReady() {
    if (!auth || !db) {
      throw new Error("ShammahFirebase.init() must be called before using Auth or Firestore");
    }
  }

  function init(config) {
    if (typeof firebase === "undefined") {
      throw new Error("Firebase compat SDK not loaded");
    }
    if (!firebase.apps || firebase.apps.length === 0) {
      firebase.initializeApp(config);
    }
    auth = firebase.auth();
    db = firebase.firestore();
  }

  /**
   * @param {(userJson: string|null) => void} callback
   * @returns {() => void} unsubscribe
   */
  function onAuthStateChanged(callback) {
    ensureReady();
    return auth.onAuthStateChanged(function (user) {
      if (user) {
        callback(JSON.stringify({
          uid: user.uid,
          isAnonymous: !!user.isAnonymous
        }));
      } else {
        callback(null);
      }
    });
  }

  /**
   * @param {(userJson: string) => void} onSuccess
   * @param {(message: string) => void} onError
   */
  function signInAnonymously(onSuccess, onError) {
    ensureReady();
    auth.signInAnonymously()
      .then(function (cred) {
        onSuccess(JSON.stringify({
          uid: cred.user.uid,
          isAnonymous: !!cred.user.isAnonymous
        }));
      })
      .catch(function (err) {
        onError(String((err && err.message) || err));
      });
  }

  /**
   * @param {() => void} onSuccess
   * @param {(message: string) => void} onError
   */
  function signOut(onSuccess, onError) {
    ensureReady();
    auth.signOut()
      .then(function () {
        onSuccess();
      })
      .catch(function (err) {
        onError(String((err && err.message) || err));
      });
  }

  function serializeField(value) {
    if (value == null) {
      return null;
    }
    // Firestore Timestamp
    if (typeof value.toDate === "function" && typeof value.seconds === "number") {
      return {
        seconds: value.seconds,
        nanoseconds: typeof value.nanoseconds === "number" ? value.nanoseconds : 0
      };
    }
    if (Array.isArray(value)) {
      return value.map(serializeField);
    }
    if (typeof value === "object") {
      var out = {};
      Object.keys(value).forEach(function (key) {
        out[key] = serializeField(value[key]);
      });
      return out;
    }
    return value;
  }

  /**
   * Live query: collection where isActive == true.
   * @param {string} collectionName
   * @param {(docsJson: string) => void} onNext
   * @param {(message: string) => void} onError
   * @returns {() => void} unsubscribe
   */
  function subscribeActiveCollection(collectionName, onNext, onError) {
    ensureReady();
    return db.collection(collectionName)
      .where("isActive", "==", true)
      .onSnapshot(
        function (snap) {
          var docs = [];
          snap.forEach(function (doc) {
            var plain = {};
            var data = doc.data() || {};
            Object.keys(data).forEach(function (key) {
              plain[key] = serializeField(data[key]);
            });
            docs.push({ id: doc.id, data: plain });
          });
          onNext(JSON.stringify(docs));
        },
        function (err) {
          var code = (err && err.code) || "";
          var message = (err && err.message) || String(err);
          onError(code + ": " + message);
        }
      );
  }

  global.ShammahFirebase = {
    init: init,
    onAuthStateChanged: onAuthStateChanged,
    signInAnonymously: signInAnonymously,
    signOut: signOut,
    subscribeActiveCollection: subscribeActiveCollection
  };
})(typeof globalThis !== "undefined" ? globalThis : window);
