import Link from "next/link";

export const metadata = {
  title: "Privacy Policy | unSheet",
  description: "Privacy Policy for unSheet Chrome Extension and Web Platform.",
};

export default function PrivacyPolicyPage() {
  return (
    <main className="min-h-screen bg-[#050608] text-gray-300 py-12 px-6 sm:px-12 max-w-4xl mx-auto font-sans">
      <div className="mb-8 border-b border-gray-800 pb-6">
        <h1 className="text-3xl font-bold text-white mb-2">Privacy Policy</h1>
        <p className="text-sm text-gray-400">Effective Date: July 30, 2026</p>
      </div>

      <div className="space-y-8 text-sm leading-relaxed">
        <section>
          <h2 className="text-lg font-semibold text-white mb-3">
            1. Introduction
          </h2>
          <p>
            Welcome to <strong>unSheet</strong> (&quot;we,&quot;
            &quot;our,&quot; or &quot;us&quot;). We respect your privacy and are
            committed to protecting the personal data and algorithm tracking
            metrics you share with us through our website and the unSheet Chrome
            Extension.
          </p>
        </section>

        <section>
          <h2 className="text-lg font-semibold text-white mb-3">
            2. Information We Collect
          </h2>
          <p className="mb-2">
            To provide automated tracking and analytics, we collect the
            following limited information:
          </p>
          <ul className="list-disc pl-5 space-y-1 text-gray-400">
            <li>
              <strong className="text-gray-200">
                Account & Authentication Data:
              </strong>{" "}
              Email address and public display name provided during Google OAuth
              login to create your account session.
            </li>
            <li>
              <strong className="text-gray-200">LeetCode Activity Data:</strong>{" "}
              Problem slugs, problem titles, difficulty levels, submission
              verdicts (e.g., Accepted, Wrong Answer), code diffs, execution
              runtime, memory usage, and timestamps captured from your LeetCode
              problem page interactions.
            </li>
            <li>
              <strong className="text-gray-200">Local Storage:</strong>{" "}
              Authentication JSON Web Tokens (JWTs) stored locally in your
              browser/extension storage to maintain session connectivity.
            </li>
          </ul>
        </section>

        <section>
          <h2 className="text-lg font-semibold text-white mb-3">
            3. How We Use Your Information
          </h2>
          <p className="mb-2">
            We use your data strictly to power the core functionality of
            unSheet:
          </p>
          <ul className="list-disc pl-5 space-y-1 text-gray-400">
            <li>
              Automatically recording and syncing your LeetCode problem
              submission attempts.
            </li>
            <li>
              Displaying personalized analytics, code diff histories, and
              recommendation queues on your unSheet dashboard.
            </li>
            <li>
              Maintaining secure access to your account via encrypted JWT
              tokens.
            </li>
          </ul>
        </section>

        <section>
          <h2 className="text-lg font-semibold text-white mb-3">
            4. Data Sharing & Third Parties
          </h2>
          <p>
            <strong>
              We do not sell, rent, trade, or monetize your personal data or
              activity history.
            </strong>
            Your information is never shared with third-party advertisers or
            data brokers. Data is transmitted securely over encrypted HTTPS
            connections solely between your browser extension, our web
            application, and our backend database infrastructure.
          </p>
        </section>

        <section>
          <h2 className="text-lg font-semibold text-white mb-3">
            5. Chrome Extension Specific Disclosure
          </h2>
          <p>
            The unSheet Chrome Extension requires access to{" "}
            <code>https://leetcode.com/*</code> solely to capture network
            responses when you execute or submit a solution on LeetCode. The
            extension does not collect background browsing history, passwords,
            or activity on any non-LeetCode websites.
          </p>
        </section>

        <section>
          <h2 className="text-lg font-semibold text-white mb-3">
            6. Data Retention & Deletion
          </h2>
          <p>
            Your submission metrics remain associated with your account so you
            can review your history. You may request account deletion or data
            removal at any time by logging out or contacting support.
          </p>
        </section>

        <section>
          <h2 className="text-lg font-semibold text-white mb-3">
            7. Contact Us
          </h2>
          <p>
            If you have questions or concerns regarding this Privacy Policy,
            please reach out via our web dashboard or project repository.
          </p>
        </section>
      </div>

      <div className="mt-12 pt-6 border-t border-gray-800 text-center text-xs text-gray-500">
        <Link href="/" className="text-blue-400 hover:underline">
          &larr; Return to unSheet Home
        </Link>
      </div>
    </main>
  );
}
